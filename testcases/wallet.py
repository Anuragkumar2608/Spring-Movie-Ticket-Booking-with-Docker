import requests
from utils import *
from utils import check_json_exists

walletServiceURL = "http://localhost:8082"

def put_wallet(userId, action, amount):
    payload = {"action":action, "amount":amount}
    response = requests.put(walletServiceURL + f"/wallets/{userId}", json=payload)
    print_request('PUT', f'/wallets/{userId}', payload)
    print_response(response)
    return response

def get_wallet(userId):
    response = requests.get(f"{walletServiceURL}/wallets/{userId}")
    print_request('GET', f'/wallets/{userId}')
    print_response(response)
    return response

def delete_wallet(userId):
    response = requests.delete(f"{walletServiceURL}/wallets/{userId}")
    print_request('DELETE', f'/wallets/{userId}')
    print_response(response)
    return response

def delete_wallets():
    response = requests.delete(walletServiceURL + "/wallets")
    print_request('DELETE', f'/wallets')
    print_response(response)
    return response

def test_get_wallet(userId, response, exists, balance = None):
    if exists:
        if not check_json_exists(response):
            return False
        
        payload = response.json()

        if not check_field_exists(payload, 'userId'):
            return False

        if not check_field_type(payload, 'userId', int):
            return False

        if not check_field_value(payload, 'userId', userId):
            return False

        if not check_field_exists(payload, 'balance'):
            return False

        if not check_field_type(payload, 'balance', int):
            return False

        if balance and not check_field_value(payload, 'balance', balance):
            return False

        if not check_fields_count(payload, 2):
            return False
    
        if not check_response_status_code(response, 200):
            return False

        return True
    else:
        if not check_response_status_code(response, 404):
            return False

        return True

def test_put_wallet(userId, action, amount, old_balance, response):
    if (action == 'debit' and old_balance < amount):
        if response.status_code == 400:
            print_pass_message(f"Insufficient balance, expected HTTP 400, got HTTP {response.status_code}")
            return True
        else:
            print_fail_message(f"Insufficient balance, expected HTTP 400, got HTTP {response.status_code}")
            return False
    
    if not check_json_exists(response):
        return False
        
    payload = response.json()

    if not check_field_exists(payload, 'userId'):
        return False

    if not check_field_type(payload, 'userId', int):
        return False

    if not check_field_value(payload, 'userId', userId):
        return False
    
    if not check_field_exists(payload, 'balance'):
        return False

    if not check_field_type(payload, 'balance', int):
        return False

    if not check_fields_count(payload, 2):
        return False
        
    if action == 'credit':
        if payload['balance'] != old_balance + amount:
            print_fail_message(f"Field 'balance' incorrectly updated, expected {old_balance + amount}, got {payload['balance']}.")
            return False
        else:
            print_pass_message(f"Field 'balance' correctly updated, expected {old_balance + amount}, got {payload['balance']}.")            

    if action == 'debit':
        if payload['balance'] != old_balance - amount:
            print_fail_message(f"Field 'balance' incorrectly updated, expected {old_balance - amount}, got {payload['balance']}.")
            return False
        else:
            print_pass_message(f"Field 'balance' correctly updated, expected {old_balance - amount}, got {payload['balance']}.")


    if not check_response_status_code(response, 200):
        return False
        
    return True

def test_delete_wallet(response, exists):
    if exists:
        if not check_response_status_code(response, 200):
            return False

        return True
    else:
        if not check_response_status_code(response, 404):
            return False

        return True
