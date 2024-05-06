package com.anurag.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<UserT> createUser(UserRequest req){
        String mail = req.getEmail();
        List<UserT> userTList = userRepo.findByEmail(mail);
        if(!userTList.isEmpty()){
            return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(null);
        }else{
            UserT userT = new UserT();
            userT.setName(req.getName());
            userT.setEmail(req.getEmail());
            userRepo.save(userT);
            userTList = userRepo.findByEmail(req.getEmail());
            return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(userTList.get(0));
        }
    }

    public ResponseEntity<Optional<UserT>> getUser(Integer userId){
        Optional<UserT> user = userRepo.findById(userId);
        if(user.isPresent()) {
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(user);
        } else {
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(null);
        }
    }

    public ResponseEntity<Void> deleteUser(Integer userId){
        Optional<UserT> user = userRepo.findById(userId);
        if(user.isEmpty()){
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).build();
        }else{
            userRepo.deleteById(userId);
            String bookingUri = "http://host.docker.internal:8081/bookings/users/"+userId;
            String walletUri = "http://host.docker.internal:8082/wallets/"+userId;
            try {
                restTemplate.delete(bookingUri);
                restTemplate.delete(walletUri);
                System.out.println("Reached here");
            }catch(Exception e){
                e.printStackTrace();
            }
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).build();
        }
    }

    public ResponseEntity<Void> deleteUsers(){
        String bookingUri = "http://host.docker.internal/bookings";
        String walletUri = "http://host.docker.internal/wallets";
        try {
            restTemplate.delete(bookingUri);
            restTemplate.delete(walletUri);
        }catch(Exception e){
            e.printStackTrace();
        }
        userRepo.deleteAll();
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).build();
    }
}
