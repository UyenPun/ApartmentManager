package com.company;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.company.entity.Resident;
import com.company.repository.ResidentRepository;

@SpringBootApplication
public class ProgramResident implements CommandLineRunner {

    @Autowired
    private ResidentRepository residentRepository;

    public static void main(String[] args) {
        SpringApplication.run(ProgramResident.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("*********** GET ALL RESIDENTS ***********");
        List<Resident> residents = residentRepository.findAll();
        for (Resident resident : residents) {
            System.out.println("Name: " + resident.getName());
            System.out.println("Email: " + resident.getEmail());
            System.out.println("Gender: " + resident.getGender().getValue());
            System.out.println("----------------");
        }
    }
}
