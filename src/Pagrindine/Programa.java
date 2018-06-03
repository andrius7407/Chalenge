package Pagrindine;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;

public class Programa {

    public static class Lentele {
        private String paymentNumber;
        private String  date;
        private String amount;
        private String principalPayment;
        private String interestPayment;
        private String totalPayment;
        private String interestRate;

        public Lentele(String paymentNumber, String date, String amount, String principalPayment,
                       String interestPayment, String totalPayment, String interestRate) {
            this.paymentNumber = paymentNumber;
            this.date = date;
            this.amount = amount;
            this.principalPayment = principalPayment;
            this.interestPayment = interestPayment;
            this.totalPayment = totalPayment;
            this.interestRate = interestRate;
        }

        @Override
        public String toString(){
            return paymentNumber+";"+date+";"+amount+";"+principalPayment+";"+
                    interestPayment+";"+totalPayment+";"+interestRate;
        }
    }
    public static Date addMonth(Date date, int i){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, i);
        return calendar.getTime();
    }
    public static String intToString (int sk){
        String string = sk + "";
        return string;
    }
    public static String doubleToString (double sk){
        String string = sk + "";
        return string;
    }
    public static String dateToString (Date date){
        String newDate;
        SimpleDateFormat sdfr = new SimpleDateFormat("dd-MM-yyyy");
        newDate = sdfr.format(date);
        return newDate;
    }
    public static Double round (double a){
        a = (double) Math.round(a * 100) / 100;
        return a;
    }

    public static void main(String[] args) throws IOException {
        String date = "15-04-2017";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date2 = null;
        try {
            date2 = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        double likoMoketi = 5000;
        double palukanuNorma = 12;

        double palukanuPerMenesi = palukanuNorma / 12 / 100;
        double mokejimuSkaicius = 24;
        /*anuitetas skaičiuojamas: mėnesio įmoka = K * kredito dydis
        K = (i * (1 + i)^n) / ((1 + i)^n - 1). i - palūkanos, n - periodų skaicius*/
        double k = (palukanuPerMenesi * Math.pow((1 + palukanuPerMenesi),mokejimuSkaicius)) /
                (Math.pow((1 + palukanuPerMenesi),mokejimuSkaicius) - 1);
        double menesineImoka = round(k * likoMoketi);
        int mokejimoNumeris = 0;
        double dengiaPaskola = 0;
        double palukanos;
        Date mokejimoData;
        List<Lentele> arrayList = new ArrayList<>();
        for (int i = 0; i < mokejimuSkaicius; i++){
            mokejimoNumeris++;
            mokejimoData = addMonth(date2, i + 1);
            likoMoketi = round(likoMoketi - dengiaPaskola);
            palukanos = round(likoMoketi * palukanuPerMenesi);
            if (mokejimoNumeris < mokejimuSkaicius){
                dengiaPaskola = round(menesineImoka - palukanos);
            }else {
                menesineImoka = round(likoMoketi + palukanos);
                dengiaPaskola = likoMoketi;
            }
            arrayList.add(new Lentele(intToString(mokejimoNumeris),dateToString(mokejimoData),doubleToString(likoMoketi),
                    doubleToString(dengiaPaskola),doubleToString(palukanos),
                    doubleToString(menesineImoka),doubleToString(palukanuNorma)));
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter("src\\kreditas.csv"));
        String[] header = new String[] {"Mok.nr","Data","Liko","Pask.mok.","Pal.mok","Mok.","Pal.n."};
        String head = String.join(";",header) ;
        bw.write(head);
        bw.newLine();
        for (Lentele lent : arrayList){
            bw.write(lent.toString());
            bw.newLine();
        }
        bw.close();
    }
}


