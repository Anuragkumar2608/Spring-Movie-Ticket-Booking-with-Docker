package com.anurag.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class BookingService {

    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private TheatreRepo theatreRepo;

    @Autowired
    private ShowRepo showRepo;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<List<Theatre>> getTheatres(){
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(theatreRepo.findAll());
    }

    public ResponseEntity<List<Show>> getShowsAtTheatre(Integer theatreId){
        Optional<Theatre> theatre = theatreRepo.findById(theatreId);
        if(theatre.isEmpty()){
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).build();
        }else{
            List<Show> shows = showRepo.findByTheatreId(theatreId);
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(shows);
        }
    }

    public ResponseEntity<Show> getShow(Integer showId){
        Optional<Show> show = showRepo.findById(showId);
        if(show.isEmpty()){
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).build();
        }else{
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(show.get());
        }
    }

    public ResponseEntity<List<Booking>> getUserBookings(@PathVariable Integer userId){
        List<Booking> userBookings = bookingRepo.findByUserId(userId);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(userBookings);
    }

    public ResponseEntity<Void> postBooking(BookingRequest request){
        Optional<Show> show = showRepo.findById(request.getShowId());
        if(show.isEmpty())return ResponseEntity.status(HttpStatusCode.valueOf(400)).build();
        String userUri = "http://host.docker.internal:8080/users/"+request.getUserId();
        try{
            Integer retCode = restTemplate.getForEntity(userUri,UserObj.class).getStatusCode().value();
            if(retCode == 404)return ResponseEntity.status(HttpStatusCode.valueOf(400)).build();
        }catch(Exception e){
            e.printStackTrace();
        }
        if(show.get().getSeatsAvailable() < request.getSeatsBooked()){
            return ResponseEntity.status(HttpStatusCode.valueOf(400)).build();
        }
        String walletUri = "http://host.docker.internal:8082/wallets/"+request.getUserId();
        try{
            WalletReqObj walletReq = new WalletReqObj();
            walletReq.setAction("debit");
            walletReq.setAmount(request.getSeatsBooked() * show.get().getPrice());
            HttpEntity<WalletReqObj> requestEntity = new HttpEntity<>(walletReq);
            ResponseEntity<String> response = restTemplate.exchange(walletUri, HttpMethod.PUT, requestEntity, String.class);
            if(response.getStatusCodeValue() == 400){
                return ResponseEntity.status(HttpStatusCode.valueOf(400)).build();
            }
        }catch(HttpClientErrorException e){
            e.printStackTrace();
            if(Integer.valueOf(e.getStatusCode().value()).equals(400))
                return ResponseEntity.status(HttpStatusCode.valueOf(400)).build();
        }
        show.get().setSeatsAvailable(show.get().getSeatsAvailable()- request.getSeatsBooked());
        System.out.println("updated show: "+ show.get());
        showRepo.save(show.get());
//        System.out.println("reached here");
        Booking booking = new Booking();
        booking.setUserId(request.getUserId());
        booking.setShowId(request.getShowId());
        booking.setSeatsBooked(request.getSeatsBooked());
        bookingRepo.save(booking);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).build();
    }

    public ResponseEntity<Void> deleteUserBookings(Integer userId){
        List<Booking> userBookings = bookingRepo.findByUserId(userId);
        Map<Show,Integer> showMap = new HashMap<>();
        Integer amount = 0;
        if(userBookings.isEmpty()){
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).build();
        }else{
            for(Booking b:userBookings){
                Optional<Show> show = showRepo.findById(b.getShowId());
                if(show.isPresent()) {
                    int curr = showMap.getOrDefault(show.get(),0);
                    amount += b.seatsBooked * show.get().getPrice();
                    showMap.put(show.get(),curr + b.seatsBooked);
                }
            }
            Set<Show> showSet= showMap.keySet();
            for(Show s: showSet){
                s.setSeatsAvailable(s.getSeatsAvailable()+showMap.get(s));
                showRepo.save(s);
            }
        }
        String walletUri = "http://host.docker.internal:8082/wallets/"+userId;
        try{
            WalletReqObj walletReq = new WalletReqObj();
            walletReq.setAction("credit");
            walletReq.setAmount(amount);
            HttpEntity<WalletReqObj> requestEntity = new HttpEntity<>(walletReq);
            ResponseEntity<String> response = restTemplate.exchange(walletUri, HttpMethod.PUT, requestEntity, String.class);
        }catch(HttpClientErrorException e){
            e.printStackTrace();
            if(Integer.valueOf(e.getStatusCode().value()).equals(400))
                return ResponseEntity.status(HttpStatusCode.valueOf(400)).build();
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).build();
    }

    public ResponseEntity<Void> deleteUserBookingsForShow(Integer userId,Integer showId){
        List<Booking> userBookings = bookingRepo.findByUserId(userId);
        System.out.println(userBookings);
        Integer amount = 0;
        Integer seatsToAdd = 0;
        Optional<Show> show = showRepo.findById(showId);
        if(userBookings.isEmpty()){
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).build();
        }else{
            for(Booking b:userBookings) {
                if (show.isPresent() && b.getShowId().equals(showId)) {
                    amount += b.getSeatsBooked() * show.get().getPrice();
                    seatsToAdd += b.getSeatsBooked();
                }
            }
            show.get().setSeatsAvailable(show.get().getSeatsAvailable() + seatsToAdd);
            showRepo.save(show.get());
        }
        String walletUri = "http://host.docker.internal:8082/wallets/"+userId;
        try{
            WalletReqObj walletReq = new WalletReqObj();
            walletReq.setAction("credit");
            walletReq.setAmount(amount);
            HttpEntity<WalletReqObj> requestEntity = new HttpEntity<>(walletReq);
            ResponseEntity<String> response = restTemplate.exchange(walletUri, HttpMethod.PUT, requestEntity, String.class);
        }catch(HttpClientErrorException e){
            e.printStackTrace();
            if(Integer.valueOf(e.getStatusCode().value()).equals(400))
                return ResponseEntity.status(HttpStatusCode.valueOf(400)).build();
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).build();
    }

    public ResponseEntity<Void> deleteBookings(){
        try {
            bookingRepo.deleteAll();
        }catch(Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).build();
    }

}
