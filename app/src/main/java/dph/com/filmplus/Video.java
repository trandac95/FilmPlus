package dph.com.filmplus;

/**
 * Created by PhongNT on 26/11/2016.
 */

public class Video {
    public Video(String mavideo, String tenvideo, String theloai) {
        this.mavideo = mavideo;
        this.tenvideo = tenvideo;
        this.theloai = theloai;
    }

    String mavideo;

    public Video(String mavideo, String tenvideo) {
        this.mavideo = mavideo;
        this.tenvideo = tenvideo;
    }

    String tenvideo;

    public String getMavideo() {
        return mavideo;
    }

    public String getTenvideo() {
        return tenvideo;
    }

    public String getTheloai() {
        return theloai;
    }

    public void setTheloai(String theloai) {
        this.theloai = theloai;
    }

    public void setTenvideo(String tenvideo) {
        this.tenvideo = tenvideo;
    }

    public void setMavideo(String mavideo) {
        this.mavideo = mavideo;
    }

    String theloai;
}
