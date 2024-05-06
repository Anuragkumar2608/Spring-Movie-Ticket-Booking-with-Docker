package com.anurag.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @GetMapping("/theatres")
    public ResponseEntity<List<Theatre>> getTheatres(){
        return bookingService.getTheatres();
    }

    @GetMapping("/shows/theatres/{theatreId}")
    public ResponseEntity<List<Show>> getShowsAtTheatre(@PathVariable Integer theatreId){
        return bookingService.getShowsAtTheatre(theatreId);
    }

    @GetMapping("/shows/{showId}")
    public ResponseEntity<Show> getShow(@PathVariable Integer showId){
        return bookingService.getShow(showId);
    }

    @GetMapping("/bookings/users/{userId}")
    public ResponseEntity<List<Booking>> getUserBookings(@PathVariable Integer userId){
        return bookingService.getUserBookings(userId);
    }

    @PostMapping("/bookings")
    public ResponseEntity<Void> postBooking(@RequestBody BookingRequest request){
        return bookingService.postBooking(request);
    }

    @DeleteMapping("/bookings/users/{userId}")
    public ResponseEntity<Void> deleteUserBookings(@PathVariable Integer userId){
        return bookingService.deleteUserBookings(userId);
    }

    @DeleteMapping("/bookings/users/{userId}/shows/{showId}")
    public ResponseEntity<Void> deleteUserBookingsForShow(@PathVariable Integer userId, @PathVariable Integer showId){
        return bookingService.deleteUserBookingsForShow(userId, showId);
    }

    @DeleteMapping("/bookings")
    public ResponseEntity<Void> deleteBookings(){
        return bookingService.deleteBookings();
    }
}
