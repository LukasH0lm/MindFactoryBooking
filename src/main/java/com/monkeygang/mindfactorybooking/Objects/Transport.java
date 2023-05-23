package com.monkeygang.mindfactorybooking.Objects;

public class Transport {

    Transport_type type;

    int id;

    String afgangsTidspunkt;

    String ankomstTidspunkt;

    public Transport(int id, String afgangsTidspunkt, String ankomstTidspunkt, Transport_type type) {

        this.id = id;
        this.afgangsTidspunkt = afgangsTidspunkt;
        this.ankomstTidspunkt = ankomstTidspunkt;
        this.type = type;

    }



    public Transport_type getType() {
        return type;
    }

    public void setType(Transport_type type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAfgangsTidspunkt() {
        return afgangsTidspunkt;
    }

    public void setAfgangsTidspunkt(String afgangsTidspunkt) {
        this.afgangsTidspunkt = afgangsTidspunkt;
    }

    public String getAnkomstTidspunkt() {
        return ankomstTidspunkt;
    }

    public void setAnkomstTidspunkt(String ankomstTidspunkt) {
        this.ankomstTidspunkt = ankomstTidspunkt;
    }
}
