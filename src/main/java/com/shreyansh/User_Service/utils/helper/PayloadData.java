package com.shreyansh.User_Service.utils.helper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayloadData {
    private Long id;
    private String email;
}
