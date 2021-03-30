
package de.apolinarski.shelly.updater.json.firmware;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "SHPLG-1",
        "SHPLG-S",
        "SHPLG-U1",
        "SHPLG2-1",
        "SHSW-1",
        "SHSW-21",
        "SHSW-25",
        "SHSW-PM",
        "SHSW-L",
        "SHAIR-1",
        "SHSW-44",
        "SHUNI-1",
        "SHEM",
        "SHEM-3",
        "SHSEN-1",
        "SHMOS-01",
        "SHGS-1",
        "SHSM-01",
        "SHHT-1",
        "SHWT-1",
        "SHDW-1",
        "SHDW-2",
        "SHSPOT-1",
        "SHCL-255",
        "SHBLB-1",
        "SHCB-1",
        "SHRGBW2",
        "SHRGBWW-01",
        "SH2LED-1",
        "SHDM-1",
        "SHDM-2",
        "SHDIMW-1",
        "SHVIN-1",
        "SHBDUO-1",
        "SHBTN-1",
        "SHBTN-2",
        "SHIX3-1"
})
@Generated("jsonschema2pojo")
public class Data {

    @JsonProperty("SHPLG-1")
    private Shplg1 shplg1;
    @JsonProperty("SHPLG-S")
    private ShplgS shplgS;
    @JsonProperty("SHPLG-U1")
    private ShplgU1 shplgU1;
    @JsonProperty("SHPLG2-1")
    private Shplg21 shplg21;
    @JsonProperty("SHSW-1")
    private Shsw1 shsw1;
    @JsonProperty("SHSW-21")
    private Shsw21 shsw21;
    @JsonProperty("SHSW-25")
    private Shsw25 shsw25;
    @JsonProperty("SHSW-PM")
    private ShswPm shswPm;
    @JsonProperty("SHSW-L")
    private ShswL shswL;
    @JsonProperty("SHAIR-1")
    private Shair1 shair1;
    @JsonProperty("SHSW-44")
    private Shsw44 shsw44;
    @JsonProperty("SHUNI-1")
    private Shuni1 shuni1;
    @JsonProperty("SHEM")
    private Shem shem;
    @JsonProperty("SHEM-3")
    private Shem3 shem3;
    @JsonProperty("SHSEN-1")
    private Shsen1 shsen1;
    @JsonProperty("SHMOS-01")
    private Shmos01 shmos01;
    @JsonProperty("SHGS-1")
    private Shgs1 shgs1;
    @JsonProperty("SHSM-01")
    private Shsm01 shsm01;
    @JsonProperty("SHHT-1")
    private Shht1 shht1;
    @JsonProperty("SHWT-1")
    private Shwt1 shwt1;
    @JsonProperty("SHDW-1")
    private Shdw1 shdw1;
    @JsonProperty("SHDW-2")
    private Shdw2 shdw2;
    @JsonProperty("SHSPOT-1")
    private Shspot1 shspot1;
    @JsonProperty("SHCL-255")
    private Shcl255 shcl255;
    @JsonProperty("SHBLB-1")
    private Shblb1 shblb1;
    @JsonProperty("SHCB-1")
    private Shcb1 shcb1;
    @JsonProperty("SHRGBW2")
    private Shrgbw2 shrgbw2;
    @JsonProperty("SHRGBWW-01")
    private Shrgbww01 shrgbww01;
    @JsonProperty("SH2LED-1")
    private Sh2led1 sh2led1;
    @JsonProperty("SHDM-1")
    private Shdm1 shdm1;
    @JsonProperty("SHDM-2")
    private Shdm2 shdm2;
    @JsonProperty("SHDIMW-1")
    private Shdimw1 shdimw1;
    @JsonProperty("SHVIN-1")
    private Shvin1 shvin1;
    @JsonProperty("SHBDUO-1")
    private Shbduo1 shbduo1;
    @JsonProperty("SHBTN-1")
    private Shbtn1 shbtn1;
    @JsonProperty("SHBTN-2")
    private Shbtn2 shbtn2;
    @JsonProperty("SHIX3-1")
    private Shix31 shix31;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("SHPLG-1")
    public Shplg1 getShplg1() {
        return shplg1;
    }

    @JsonProperty("SHPLG-1")
    public void setShplg1(Shplg1 shplg1) {
        this.shplg1 = shplg1;
    }

    @JsonProperty("SHPLG-S")
    public ShplgS getShplgS() {
        return shplgS;
    }

    @JsonProperty("SHPLG-S")
    public void setShplgS(ShplgS shplgS) {
        this.shplgS = shplgS;
    }

    @JsonProperty("SHPLG-U1")
    public ShplgU1 getShplgU1() {
        return shplgU1;
    }

    @JsonProperty("SHPLG-U1")
    public void setShplgU1(ShplgU1 shplgU1) {
        this.shplgU1 = shplgU1;
    }

    @JsonProperty("SHPLG2-1")
    public Shplg21 getShplg21() {
        return shplg21;
    }

    @JsonProperty("SHPLG2-1")
    public void setShplg21(Shplg21 shplg21) {
        this.shplg21 = shplg21;
    }

    @JsonProperty("SHSW-1")
    public Shsw1 getShsw1() {
        return shsw1;
    }

    @JsonProperty("SHSW-1")
    public void setShsw1(Shsw1 shsw1) {
        this.shsw1 = shsw1;
    }

    @JsonProperty("SHSW-21")
    public Shsw21 getShsw21() {
        return shsw21;
    }

    @JsonProperty("SHSW-21")
    public void setShsw21(Shsw21 shsw21) {
        this.shsw21 = shsw21;
    }

    @JsonProperty("SHSW-25")
    public Shsw25 getShsw25() {
        return shsw25;
    }

    @JsonProperty("SHSW-25")
    public void setShsw25(Shsw25 shsw25) {
        this.shsw25 = shsw25;
    }

    @JsonProperty("SHSW-PM")
    public ShswPm getShswPm() {
        return shswPm;
    }

    @JsonProperty("SHSW-PM")
    public void setShswPm(ShswPm shswPm) {
        this.shswPm = shswPm;
    }

    @JsonProperty("SHSW-L")
    public ShswL getShswL() {
        return shswL;
    }

    @JsonProperty("SHSW-L")
    public void setShswL(ShswL shswL) {
        this.shswL = shswL;
    }

    @JsonProperty("SHAIR-1")
    public Shair1 getShair1() {
        return shair1;
    }

    @JsonProperty("SHAIR-1")
    public void setShair1(Shair1 shair1) {
        this.shair1 = shair1;
    }

    @JsonProperty("SHSW-44")
    public Shsw44 getShsw44() {
        return shsw44;
    }

    @JsonProperty("SHSW-44")
    public void setShsw44(Shsw44 shsw44) {
        this.shsw44 = shsw44;
    }

    @JsonProperty("SHUNI-1")
    public Shuni1 getShuni1() {
        return shuni1;
    }

    @JsonProperty("SHUNI-1")
    public void setShuni1(Shuni1 shuni1) {
        this.shuni1 = shuni1;
    }

    @JsonProperty("SHEM")
    public Shem getShem() {
        return shem;
    }

    @JsonProperty("SHEM")
    public void setShem(Shem shem) {
        this.shem = shem;
    }

    @JsonProperty("SHEM-3")
    public Shem3 getShem3() {
        return shem3;
    }

    @JsonProperty("SHEM-3")
    public void setShem3(Shem3 shem3) {
        this.shem3 = shem3;
    }

    @JsonProperty("SHSEN-1")
    public Shsen1 getShsen1() {
        return shsen1;
    }

    @JsonProperty("SHSEN-1")
    public void setShsen1(Shsen1 shsen1) {
        this.shsen1 = shsen1;
    }

    @JsonProperty("SHMOS-01")
    public Shmos01 getShmos01() {
        return shmos01;
    }

    @JsonProperty("SHMOS-01")
    public void setShmos01(Shmos01 shmos01) {
        this.shmos01 = shmos01;
    }

    @JsonProperty("SHGS-1")
    public Shgs1 getShgs1() {
        return shgs1;
    }

    @JsonProperty("SHGS-1")
    public void setShgs1(Shgs1 shgs1) {
        this.shgs1 = shgs1;
    }

    @JsonProperty("SHSM-01")
    public Shsm01 getShsm01() {
        return shsm01;
    }

    @JsonProperty("SHSM-01")
    public void setShsm01(Shsm01 shsm01) {
        this.shsm01 = shsm01;
    }

    @JsonProperty("SHHT-1")
    public Shht1 getShht1() {
        return shht1;
    }

    @JsonProperty("SHHT-1")
    public void setShht1(Shht1 shht1) {
        this.shht1 = shht1;
    }

    @JsonProperty("SHWT-1")
    public Shwt1 getShwt1() {
        return shwt1;
    }

    @JsonProperty("SHWT-1")
    public void setShwt1(Shwt1 shwt1) {
        this.shwt1 = shwt1;
    }

    @JsonProperty("SHDW-1")
    public Shdw1 getShdw1() {
        return shdw1;
    }

    @JsonProperty("SHDW-1")
    public void setShdw1(Shdw1 shdw1) {
        this.shdw1 = shdw1;
    }

    @JsonProperty("SHDW-2")
    public Shdw2 getShdw2() {
        return shdw2;
    }

    @JsonProperty("SHDW-2")
    public void setShdw2(Shdw2 shdw2) {
        this.shdw2 = shdw2;
    }

    @JsonProperty("SHSPOT-1")
    public Shspot1 getShspot1() {
        return shspot1;
    }

    @JsonProperty("SHSPOT-1")
    public void setShspot1(Shspot1 shspot1) {
        this.shspot1 = shspot1;
    }

    @JsonProperty("SHCL-255")
    public Shcl255 getShcl255() {
        return shcl255;
    }

    @JsonProperty("SHCL-255")
    public void setShcl255(Shcl255 shcl255) {
        this.shcl255 = shcl255;
    }

    @JsonProperty("SHBLB-1")
    public Shblb1 getShblb1() {
        return shblb1;
    }

    @JsonProperty("SHBLB-1")
    public void setShblb1(Shblb1 shblb1) {
        this.shblb1 = shblb1;
    }

    @JsonProperty("SHCB-1")
    public Shcb1 getShcb1() {
        return shcb1;
    }

    @JsonProperty("SHCB-1")
    public void setShcb1(Shcb1 shcb1) {
        this.shcb1 = shcb1;
    }

    @JsonProperty("SHRGBW2")
    public Shrgbw2 getShrgbw2() {
        return shrgbw2;
    }

    @JsonProperty("SHRGBW2")
    public void setShrgbw2(Shrgbw2 shrgbw2) {
        this.shrgbw2 = shrgbw2;
    }

    @JsonProperty("SHRGBWW-01")
    public Shrgbww01 getShrgbww01() {
        return shrgbww01;
    }

    @JsonProperty("SHRGBWW-01")
    public void setShrgbww01(Shrgbww01 shrgbww01) {
        this.shrgbww01 = shrgbww01;
    }

    @JsonProperty("SH2LED-1")
    public Sh2led1 getSh2led1() {
        return sh2led1;
    }

    @JsonProperty("SH2LED-1")
    public void setSh2led1(Sh2led1 sh2led1) {
        this.sh2led1 = sh2led1;
    }

    @JsonProperty("SHDM-1")
    public Shdm1 getShdm1() {
        return shdm1;
    }

    @JsonProperty("SHDM-1")
    public void setShdm1(Shdm1 shdm1) {
        this.shdm1 = shdm1;
    }

    @JsonProperty("SHDM-2")
    public Shdm2 getShdm2() {
        return shdm2;
    }

    @JsonProperty("SHDM-2")
    public void setShdm2(Shdm2 shdm2) {
        this.shdm2 = shdm2;
    }

    @JsonProperty("SHDIMW-1")
    public Shdimw1 getShdimw1() {
        return shdimw1;
    }

    @JsonProperty("SHDIMW-1")
    public void setShdimw1(Shdimw1 shdimw1) {
        this.shdimw1 = shdimw1;
    }

    @JsonProperty("SHVIN-1")
    public Shvin1 getShvin1() {
        return shvin1;
    }

    @JsonProperty("SHVIN-1")
    public void setShvin1(Shvin1 shvin1) {
        this.shvin1 = shvin1;
    }

    @JsonProperty("SHBDUO-1")
    public Shbduo1 getShbduo1() {
        return shbduo1;
    }

    @JsonProperty("SHBDUO-1")
    public void setShbduo1(Shbduo1 shbduo1) {
        this.shbduo1 = shbduo1;
    }

    @JsonProperty("SHBTN-1")
    public Shbtn1 getShbtn1() {
        return shbtn1;
    }

    @JsonProperty("SHBTN-1")
    public void setShbtn1(Shbtn1 shbtn1) {
        this.shbtn1 = shbtn1;
    }

    @JsonProperty("SHBTN-2")
    public Shbtn2 getShbtn2() {
        return shbtn2;
    }

    @JsonProperty("SHBTN-2")
    public void setShbtn2(Shbtn2 shbtn2) {
        this.shbtn2 = shbtn2;
    }

    @JsonProperty("SHIX3-1")
    public Shix31 getShix31() {
        return shix31;
    }

    @JsonProperty("SHIX3-1")
    public void setShix31(Shix31 shix31) {
        this.shix31 = shix31;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
