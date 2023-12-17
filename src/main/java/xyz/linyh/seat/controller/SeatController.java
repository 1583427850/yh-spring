package xyz.linyh.seat.controller;

import xyz.linyh.yhspring.annotation.YhController;
import xyz.linyh.yhspring.annotation.YhGetMapping;
import xyz.linyh.yhspring.annotation.YhRequestMapping;

@YhController
@YhRequestMapping("/seat")
public class SeatController {

    @YhGetMapping("/get")
    public String seat(Long seatId){
        System.out.println(seatId);
        return String.valueOf(seatId);
    }
}
