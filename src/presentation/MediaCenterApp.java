package presentation;
import business.MediaCenter_Facade;

public class MediaCenterApp {

    public static void main(String[] args){
        MediaCenter_Facade fc = new MediaCenter_Facade();
        MainPage mp = new MainPage(fc);      
        mp.setVisible(true);
    }
}