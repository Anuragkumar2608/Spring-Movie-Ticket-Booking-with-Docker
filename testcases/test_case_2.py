import requests

userServiceURL = "http://localhost:8080"
bookingServiceURL = "http://localhost:8081"
walletServiceURL = "http://localhost:8082"

def main():
    name = "John Doe"
    email = "johndoe@mail.com"
    showID = 7
    walletAmount = 1000
    add_money_and_check_detail(name, email, showID, walletAmount)

def create_user(name, email):
    new_user = {"name": name, "email": email}
    response = requests.post(userServiceURL + "/users", json=new_user)
    return response

def get_wallet(userId):
    response = requests.get(walletServiceURL + f"/wallets/{userId}")
    return response

def update_wallet(userId, action, amount):
    response = requests.put(walletServiceURL + f"/wallets/{userId}", json={"action":action, "amount":amount})
    return response

def get_show_details(showId):
    response = requests.get(bookingServiceURL + f"/shows/{showId}")
    return response   


def delete_show(userId,showId):
    response = requests.delete(bookingServiceURL+f"/bookings/users/{userId}/shows/{showId}")
    return response

def delete_users():
    requests.delete(userServiceURL+f"/users")  


def add_money_and_check_detail(name, email, showID, walletAmount):
    try:
        delete_users()
        new_user = create_user(name,email)
        new_userid = new_user.json()['id']
        response_update_wallet = update_wallet(new_userid,"credit",walletAmount)
        print(f"<= update_wallet() response: {response_update_wallet.json()}")
        show_details_before_booking = get_show_details(showID)
        print(f"<= show_details_before_booking: {show_details_before_booking.json()}")
        old_wallet_balance = get_wallet(new_userid).json()
        print(f"old_wallet_balance: {old_wallet_balance}")
        new_booking = {"showId": showID, "userId": new_userid, "seatsBooked": 10}
        requests.post(bookingServiceURL + "/bookings", json=new_booking)
        show_details_after_booking = get_show_details(showID)
        print(f"<= show_details_after_booking: {show_details_after_booking.json()}")
        new_wallet_balance = get_wallet(new_userid).json()
        print(f"new_wallet_balance: {new_wallet_balance}")
        if((old_wallet_balance['balance'] - new_wallet_balance['balance'] == show_details_after_booking.json()['price']* 10) and (show_details_before_booking.json()['seatsAvailable'] - show_details_after_booking.json()['seatsAvailable'] == 10)):
            print("Test Passed")
        else:
            print("Test Failed")
    except:
        print("Some Exception Occurred")

if __name__ == "__main__":
    main()
