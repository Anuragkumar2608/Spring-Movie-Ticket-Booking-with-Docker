import requests
from utils import *

bookingServiceURL = "http://localhost:8081"

def get_show(showId):
    response = requests.get(bookingServiceURL + f"/shows/{showId}")
    print_request('GET', f'/shows/{showId}')
    print_response(response)
    return response

def get_bookings(userId):
    response = requests.get(bookingServiceURL + f"/bookings/users/{userId}")
    print_request('GET', f'/bookings/users/{userId}')
    print_response(response)
    return response

def post_booking(userId, showId, seatsBooked):
    payload = {"showId":showId, "userId":userId, "seatsBooked":seatsBooked}
    response = requests.post(bookingServiceURL + "/bookings", json = payload)
    print_request('POST', f'/bookings', payload)
    print_response(response)
    return response

def delete_booking(userId):
    response = requests.delete(bookingServiceURL + f"/bookings/users/{userId}")
    print_request('DELETE', f'/bookings/users/{userId}')
    print_response(response)
    return response   

def delete_show(userId,showId):
    response = requests.delete(bookingServiceURL+f"/bookings/users/{userId}/shows/{showId}")
    print_request('DELETE', f'/bookings/users/{userId}/shows/{showId}')
    print_response(response)
    return response

def delete_bookings():
    response = requests.delete(bookingServiceURL + "/bookings")
    print_request('DELETE', f'/bookings')
    print_response(response)
    return response

def test_get_show(response):
    if not check_json_exists(response):
        return False

    payload = response.json()
    
    if not check_field_exists(payload, 'id'):
        return False

    if not check_field_type(payload, 'id', int):
        return False

    if not check_field_exists(payload, 'theatreId'):
        return False

    if not check_field_type(payload, 'theatreId', int):
        return False

    if not check_field_exists(payload, 'title'):
        return False

    if not check_field_type(payload, 'title', str):
        return False

    if not check_field_exists(payload, 'price'):
        return False

    if not check_field_type(payload, 'price', int):
        return False

    if not check_field_exists(payload, 'seatsAvailable'):
        return False

    if not check_field_type(payload, 'seatsAvailable', int):
        return False

    if not check_fields_count(payload, 5):
        return False
    
    if not check_response_status_code(response, 200):
        return False

    return True


def test_post_booking(booking_response, show_before, show_after, wallet_before, wallet_after, seatsBooked):
    if not check_field_value(show_before, 'id', show_after['id']):
        return False

    price = show_before['price']
    seatsAvailable = show_before['seatsAvailable']
    balance_available = wallet_before['balance']

    can_book = seatsAvailable >= seatsBooked and balance_available >= (seatsBooked * price)

    if not can_book:
        if not check_field_value(show_after, 'seatsAvailable', seatsAvailable):
            return False

        if not check_field_value(wallet_after, 'balance', balance_available):
            return False

        if not check_response_status_code(booking_response, 400):
            return False

        return True
    else:
        if not check_field_value(show_after, 'seatsAvailable', seatsAvailable - seatsBooked):
            return False

        if not check_field_value(wallet_after, 'balance', balance_available - price * seatsBooked):
            return False

        if not check_response_status_code(booking_response, 200):
            return False

        return True
    
        
            
