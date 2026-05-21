package com.FYP.Fleet.Dto.Response;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DriverResponseDto {

    private long id;
    private String name;
    private String phone;
    private Long userId;
    private String ownerName;
    private List<Long> tripList;
}
