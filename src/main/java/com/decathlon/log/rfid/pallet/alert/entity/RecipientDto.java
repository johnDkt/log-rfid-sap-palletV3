package com.decathlon.log.rfid.pallet.alert.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecipientDto {

    private String name;
    private String address;
}
