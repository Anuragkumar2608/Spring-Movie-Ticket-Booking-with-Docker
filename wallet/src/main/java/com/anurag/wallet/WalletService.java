package com.anurag.wallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WalletService {

    @Autowired
    WalletRepo walletRepo;

    public ResponseEntity<Optional<Wallet>> getUserWallet(Integer userId){
        Optional<Wallet> wallet = walletRepo.findById(userId);
        if(wallet.isPresent()){
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(Optional.of(wallet.get()));
        }else{
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(null);
        }
    }

    public ResponseEntity<Optional<Wallet>> updateUserWallet(Integer userId,WalletRequest request){
        Optional<Wallet> wallet = walletRepo.findById(userId);
        Wallet inWallet = new Wallet();
        if(wallet.isEmpty()){
            inWallet.setUserId(userId);
            inWallet.setBalance(0);
            walletRepo.save(inWallet);
        }else{
            inWallet.setUserId(wallet.get().getUserId());
            inWallet.setBalance(wallet.get().getBalance());
        }
        if(request.getAction().equals("debit")){
            if(inWallet.getBalance() < request.getAmount()){
                Wallet empWallet = new Wallet();
                return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(Optional.of(empWallet));
            }else{
                inWallet.setBalance(inWallet.getBalance()- request.getAmount());
                walletRepo.save(inWallet);
                return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(Optional.of(inWallet));
            }
        }else{
            inWallet.setBalance(inWallet.getBalance() + request.getAmount());
            walletRepo.save(inWallet);
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(Optional.of(inWallet));
        }
    }

    public ResponseEntity<Void> deleteUserWallet(Integer userId){
        Optional<Wallet> wallet = walletRepo.findById(userId);
        if(wallet.isPresent()){
            System.out.println("wallet: "+wallet.get());
            walletRepo.deleteById(userId);
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).build();
        }else{
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).build();
        }
    }

    public ResponseEntity<Void> deleteWallets(){
        walletRepo.deleteAll();
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).build();
    }
}
