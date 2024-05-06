package com.anurag.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserObj {
    private Integer id;

    private String name;

    private String email;
}
