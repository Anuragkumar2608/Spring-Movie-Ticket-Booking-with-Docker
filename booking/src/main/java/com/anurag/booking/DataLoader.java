package com.anurag.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    TheatreRepo theatreRepo;

    @Autowired
    ShowRepo showRepo;

    @Override
    public void run(String... args) throws Exception {
        ClassPathResource showRes = new ClassPathResource("static/shows.csv");
        InputStream iStream = showRes.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
        String line = "";
        int i = 0;
        while((line = br.readLine()) != null){
//            System.out.println(line);
            if(i==0){
                i++;
                continue;
            }
            String[] values = line.split(",");
            Integer id = Integer.valueOf(values[0]);
            Integer theatreId = Integer.valueOf(values[1]);
            String title = values[2];
            Integer price = Integer.valueOf(values[3]);
            Integer seatsAvailable = Integer.valueOf(values[4]);
            Show show = new Show(id, theatreId, title, price, seatsAvailable);
            showRepo.save(show);
            System.out.println(id+","+theatreId+","+title+","+price+","+seatsAvailable);
        }

        ClassPathResource theatreRes = new ClassPathResource("static/theatres.csv");
        iStream = theatreRes.getInputStream();
        br = new BufferedReader(new InputStreamReader(iStream));
        i = 0;
        while((line = br.readLine()) != null){
//            System.out.println(line);
            if(i==0){
                i++;
                continue;
            }
            String[] values = line.split(",");
            Integer id = Integer.valueOf(values[0]);
            String name = values[1];
            String location = values[2];
            Theatre theatre = new Theatre(id,name,location);
            theatreRepo.save(theatre);
            System.out.println(id+","+name+","+location);
        }
    }
}
