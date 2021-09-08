package service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class IncomingAddressParser {

    private String name;
    private String lastName;
    private String pn;
    private String streetAddress;
    private String postalCode;
    private String postalAddress;

    public IncomingAddressParser(String [] incomingMessage){
        this.name = incomingMessage[0].trim();
        this.lastName = incomingMessage[1].trim();
        this.pn = Base64.getEncoder().encodeToString(incomingMessage[2].trim().getBytes(StandardCharsets.UTF_8));
        this.streetAddress = incomingMessage[3].trim();
        this.postalCode = incomingMessage[4].trim();
        this.postalAddress = incomingMessage[5].trim();
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPn() {
        return pn;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPostalAddress() {
        return postalAddress;
    }
}
