package com.anurag.booking;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Show {
    @Id
    private Integer id;

    private Integer theatreId;

    private String title;

    private Integer price;

    private Integer seatsAvailable;
}
