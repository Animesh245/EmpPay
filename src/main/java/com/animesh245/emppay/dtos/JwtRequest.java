package com.animesh245.emppay.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JwtRequest
{
    String username;
    String password;
}
