package com.iba.springboot07;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Order {

    @Id private String uuid;
    private String companyId;
    private String siteName;
    private String contactName;

    public Order() {
        this.uuid = UUID.randomUUID().toString();
    }

    public Order(String siteName) {
        this.uuid = UUID.randomUUID().toString();
        this.siteName = siteName;
    }


    @Override public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();

        String result;

        try {
            result = objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            result = "{}";
        }
        return this.getClass().getSimpleName() + result;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteName() {
        return siteName;
    }
}
