package com.example.demo.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDataEvent {

    private List<BookToDecrement> books;
}
