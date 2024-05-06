package com.anurag.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
    private Integer showId;
    private Integer userId;
    private Integer seatsBooked;
}
