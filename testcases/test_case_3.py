import requests

userServiceURL = "http://localhost:8080"
bookingServiceURL = "http://localhost:8081"
walletServiceURL = "http://localhost:8082"

def main():
    name = "John Doe"
    email = "johndoe@mail.com"
    showID = 7
    add_money_and_book_ticket(name, email, showID)

def create_user(name, email):
    new_user = {"name": name, "email": email}
    response = requests.post(userServiceURL + "/users", json=new_user)
    return response

def get_wallet(user_id):
    response = requests.get(walletServiceURL + f"/wallets/{user_id}")
    return response

def update_wallet(user_id, action, amount):
    response = requests.put(walletServiceURL + f"/wallets/{user_id}", json={"action":action, "amount":amount})
    return response

def get_show_details(show_id):
    response = requests.get(bookingServiceURL + f"/shows/{show_id}")
    return response   


def delete_show(user_id,show_id):
    response = requests.delete(bookingServiceURL+f"/bookings/users/{user_id}/shows/{show_id}")
    return response

def delete_users():
    requests.delete(userServiceURL+f"/users")    

def add_money_and_book_ticket(name,email,showID):
    try:
        delete_users()
        new_user = create_user(name,email) #create_user
        new_userid = new_user.json()['id']
        print(f"wallet_before_update: {get_wallet(new_userid)}")
        response_update_wallet = update_wallet(new_userid,"credit",1000) #update_wallet
        print(f"<= update_wallet() response: {response_update_wallet.json()}")
        show_details_before_booking = get_show_details(showID) #get_show_details
        print(f"<= show_details_before_booking: {show_details_before_booking.json()}")
        old_wallet_balance = get_wallet(new_userid).json()['balance'] #get_wallet
        print(f"old_wallet_balance: {old_wallet_balance}")
        new_booking = {"showId": showID, "userId": new_userid, "seatsBooked": 10}
        requests.post(bookingServiceURL + "/bookings", json=new_booking)
        wallet_balance_after_booking = get_wallet(new_userid).json()['balance']
        print(f"wallet_balance_after_booking: {wallet_balance_after_booking}")
        del_res = delete_show(new_userid,showID) #delete_show
        show_details_after_booking = get_show_details(showID)
        print(f"show_details_after_booking: {show_details_after_booking.json()}")
        if(show_details_after_booking.json()['seatsAvailable'] == show_details_before_booking.json()['seatsAvailable'] and old_wallet_balance == get_wallet(new_userid).json()['balance']):
            print("Test passed")
        else:
            print("Test failed")
    except:
        print("Some Exception Occurred")

if __name__ == "__main__":
    main()
