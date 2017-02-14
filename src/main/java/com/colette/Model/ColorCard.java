package com.colette.model;

import com.fasterxml.jackson.annotation.JsonView;
import utils.view.ColorView;

import javax.persistence.*;

@Entity
@Table(name = "color")
@SequenceGenerator(name = "color_seq", sequenceName = "color_seq", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID"))
public class ColorCard {

    @Id
    @Column(name = "id", insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(ColorView.AllInfoLevel.class)
    private long id;

    @Column(name = "percent")
    @JsonView(ColorView.MediumInfoLevel.class)
    private String percent;

    @Column(name = "hex")
    @JsonView(ColorView.MediumInfoLevel.class)
    private String hex;

    @Column(name = "RGB")
    @JsonView(ColorView.MediumInfoLevel.class)
    private String rgb;

    @Column(name = "red")
    @JsonView(ColorView.AllInfoLevel.class)
    private int red;

    @Column(name = "green")
    @JsonView(ColorView.AllInfoLevel.class)
    private int green;

    @Column(name = "blue")
    @JsonView(ColorView.AllInfoLevel.class)
    private int blue;

    public ColorCard() {
    }

    public ColorCard(String percent, String hex, String rgb) {
        this.percent = percent;
        this.hex = hex;
        this.rgb = rgb;
    }

    public ColorCard(String percent, String hex, String rgb, int red, int green, int blue) {
        this.percent = percent;
        this.hex = hex;
        this.rgb = rgb;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public String getRgb() {
        return rgb;
    }

    public void setRgb(String rgb) {
        this.rgb = rgb;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ColorCard colorCard = (ColorCard) o;

        if (id != colorCard.id) return false;
        if (red != colorCard.red) return false;
        if (green != colorCard.green) return false;
        if (blue != colorCard.blue) return false;
        if (percent != null ? !percent.equals(colorCard.percent) : colorCard.percent != null) return false;
        if (hex != null ? !hex.equals(colorCard.hex) : colorCard.hex != null) return false;
        return rgb != null ? rgb.equals(colorCard.rgb) : colorCard.rgb == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (percent != null ? percent.hashCode() : 0);
        result = 31 * result + (hex != null ? hex.hashCode() : 0);
        result = 31 * result + (rgb != null ? rgb.hashCode() : 0);
        result = 31 * result + red;
        result = 31 * result + green;
        result = 31 * result + blue;
        return result;
    }

    @Override
    public String toString() {
        return "ColorCard{" +
                "id=" + id +
                ", percent='" + percent + '\'' +
                ", hex='" + hex + '\'' +
                ", rgb='" + rgb + '\'' +
                ", red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                '}';
    }
}
