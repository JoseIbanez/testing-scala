package com.iba.springboot07;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
//import javax.validation.constraints.NotEmpty;


@RestController
@RequestMapping("/v1")
public class DummyController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(
            method = RequestMethod.GET,
            path = "/order/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity getOrderById(@PathVariable("id") String id) {
        logger.debug("IN: Id {}", id);

        Order o1 = new Order();
        o1.setUuid("33");
        o1.setSiteName("aaaa");

        Order o2 = new Order("aaaa");

        logger.info(o1.toString());
        logger.info(o2.toString());

        return ResponseEntity.ok(o2);
    }



    @RequestMapping(
            method = RequestMethod.GET,
            path = "/dummy/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity getVodafoneId(@PathVariable("id") String id) {
        logger.warn("getId :: {} -> Id", id);
        logger.debug("debug debug");

        Employee employee = new Employee("e1", "r1");
        List<Employee> l1 = new ArrayList<Employee>();
        l1.add(employee);
        l1.add(employee);
        logger.info(employee.toString());
        logger.info(l1.toString());
        return ResponseEntity.ok(l1);
    }
}