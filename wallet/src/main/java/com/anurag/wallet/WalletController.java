package com.anurag.wallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class WalletController {

    @Autowired
    WalletService walletService;

    @GetMapping("/wallets/{userId}")
    public ResponseEntity<Optional<Wallet>> getUserWallet(@PathVariable Integer userId){
        return walletService.getUserWallet(userId);
    }

    @PutMapping("/wallets/{userId}")
    public ResponseEntity<Optional<Wallet>> updateUserWallet(@PathVariable Integer userId, @RequestBody WalletRequest request){
        return walletService.updateUserWallet(userId, request);
    }

    @DeleteMapping("/wallets/{userId}")
    public ResponseEntity<Void> deleteUserWallet(@PathVariable Integer userId){
        return walletService.deleteUserWallet(userId);
    }

    @DeleteMapping("/wallets")
    public ResponseEntity<Void> deleteWallets(){
        return walletService.deleteWallets();
    }

}
