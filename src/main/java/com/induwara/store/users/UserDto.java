package com.induwara.store.users;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDto {
    private Long id;
//    @JsonProperty("username")
    private String name;
    private String email;
//    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")
//    private LocalDateTime createdAt;
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private String phoneNumber;
}
