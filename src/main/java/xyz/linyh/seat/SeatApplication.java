package xyz.linyh.seat;

import xyz.linyh.yhspring.YhApplicationRun;
import xyz.linyh.yhspring.annotation.ComponentScan;

@ComponentScan
public class SeatApplication {
    public static void main(String[] args) {
        YhApplicationRun.run(args);
    }
}
