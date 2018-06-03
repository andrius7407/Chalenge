package PapildomaPirma;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Papild1 {
    public static class Mokejimai {
        private String paymentNumber;
        private String date;
        private String amount;
        private String principalPayment;
        private String interestPayment;
        private String totalPayment;
        private String interestRate;

        public Mokejimai(String paymentNumber, String date, String amount,
                         String principalPayment, String interestPayment,
                         String totalPayment, String interestRate) {
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

    private static Date stringToDate(String dat) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(dat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private static double kiekMenesiu(String start, String end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate from = LocalDate.parse(start, formatter);
        LocalDate to = LocalDate.parse(end, formatter);
        Period age = Period.between(from, to);
        double months = age.getMonths();
        return months;
    }

    private static double kiekDienu(String start, String end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate from = LocalDate.parse(start, formatter);
        LocalDate to = LocalDate.parse(end, formatter);
        Period age = Period.between(from, to);
        double days = age.getDays();
        return days;
    }

    private static Date pridetiMenesi(Date date, double d) {
        int i = (int)d;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, i);
        return calendar.getTime();
    }

    public static String dateToString(Date date) {
        String newDate;
        SimpleDateFormat sdfr = new SimpleDateFormat("dd-MM-yyyy");
        newDate = sdfr.format(date);
        return newDate;
    }
    public static Double round (double a){
        a = (double) Math.round(a * 100) / 100;
        return a;
    }

    public static double moketiPerMen (double palukanuPerMen,double mokejimuSkaicius,double likoMoketi){
         /*anuitetas skaičiuojamas: mėnesio įmoka = K * kredito dydis
        K = (i * (1 + n)^n) / ((1 + i)^n - 1). i - men.palūkanos, n - periodų skaicius
        */
        double k = (palukanuPerMen * Math.pow((1 + palukanuPerMen), mokejimuSkaicius)) /
                (Math.pow((1 + palukanuPerMen), mokejimuSkaicius) - 1);
        double menesineImoka = k * likoMoketi;
        menesineImoka = (double) Math.round(menesineImoka * 100) / 100;
        return menesineImoka;
    }
    public static String doubleToString (double sk) {
        String string = sk + "";
        return string;
    }
    public static void main(String[] args) throws ParseException, IOException {

        String start = "15-04-2017";
        Date date11 = stringToDate(start);
        String end = "02-09-2017";

        Double menIkiPakeitimo = kiekMenesiu(start, end);
        Date keitimo1 = pridetiMenesi(date11, menIkiPakeitimo);
        Date keitimo2 = pridetiMenesi(date11, menIkiPakeitimo + 1);

        double keitimoMenDienuiki = kiekDienu(dateToString(keitimo1), end);
        double keitimoMenDienuPo = kiekDienu(end, dateToString(keitimo2));
        double keitimoMenDienu = keitimoMenDienuiki + keitimoMenDienuPo;

        double likoMoketi = 5000;
        double palNorm1Iki = 12;
        double palukanuPerMenIki = palNorm1Iki / 12 / 100;
        String palukanuNormaIki = Double.toString(palNorm1Iki);
        double palNormPo = 9;
        double palukanuPerMenPo = palNormPo / 12 / 100;
        String palukanuNormaPo = Double.toString(palNormPo);
        double mokejimuSkaiciusIki = 24;
        double mokejimuSkaiciusPo = round(mokejimuSkaiciusIki - menIkiPakeitimo - 1 / keitimoMenDienu * keitimoMenDienuiki);

        double palukanuMenIki = palukanuPerMenIki * (keitimoMenDienuiki / keitimoMenDienu);
        double palukanuMenPo = palukanuPerMenPo * (keitimoMenDienuPo / keitimoMenDienu);
        double menesineImokaIki = moketiPerMen(palukanuPerMenIki,mokejimuSkaiciusIki,likoMoketi);

        double mokejimoNumeris = 0;
        double dengiaPaskola = 0;
        double palukanos;
        Date mokejimoData;
        double menesineImoka;
        String palukanuNorma;
        String pNormaTarp = palukanuNormaIki + "," + palukanuNormaPo;

        List<Mokejimai> arrayList = new ArrayList<>();
        for (int i = 0; i < menIkiPakeitimo; i++) {
            mokejimoNumeris++;
            mokejimoData = pridetiMenesi(date11, mokejimoNumeris);
            likoMoketi = round(likoMoketi - dengiaPaskola);
            palukanos = round(likoMoketi * palukanuPerMenIki);
            dengiaPaskola = round(menesineImokaIki - palukanos);
            menesineImoka = menesineImokaIki;
            palukanuNorma = palukanuNormaIki;
            arrayList.add(new Mokejimai(doubleToString(mokejimoNumeris),dateToString(mokejimoData),doubleToString(likoMoketi),
                    doubleToString(dengiaPaskola),doubleToString(palukanos),doubleToString(menesineImoka),palukanuNorma));
        }
        mokejimoNumeris++;
        mokejimoData = pridetiMenesi(date11, mokejimoNumeris);
        likoMoketi = round(likoMoketi - dengiaPaskola);
        double palukanuIki1 = likoMoketi * palukanuMenIki;
        double dengiaPaskola1 = menesineImokaIki / keitimoMenDienu * keitimoMenDienuiki - palukanuIki1;
        double likoMoketi1 = round(likoMoketi - dengiaPaskola1);
        double menesineImokaPo = moketiPerMen(palukanuPerMenPo,mokejimuSkaiciusPo,likoMoketi1);
        double palukanuIki2 = likoMoketi1 * palukanuMenPo;
        double dengiaPaskola2 = menesineImokaPo / keitimoMenDienu * keitimoMenDienuPo - palukanuIki2;
        double likoMoketi2 = round(likoMoketi1 - dengiaPaskola2);
        palukanos = round(palukanuIki1 + palukanuIki2);
        menesineImoka = round(menesineImokaIki / keitimoMenDienu * keitimoMenDienuiki +
                menesineImokaPo / keitimoMenDienu * keitimoMenDienuPo);
        dengiaPaskola = round(dengiaPaskola1 + dengiaPaskola2);
        palukanuNorma = pNormaTarp;
        arrayList.add(new Mokejimai(doubleToString(mokejimoNumeris),dateToString(mokejimoData), doubleToString(likoMoketi),
                doubleToString(dengiaPaskola),doubleToString(palukanos), doubleToString(menesineImoka),palukanuNorma));

        double n = mokejimuSkaiciusIki - menIkiPakeitimo - 1;
        for (int i = 0; i < n; i++) {
            mokejimoNumeris++;
            mokejimoData = pridetiMenesi(date11, mokejimoNumeris);
            likoMoketi = round(likoMoketi - dengiaPaskola);
            palukanos = round(likoMoketi * palukanuPerMenPo);
            menesineImoka = menesineImokaPo ;
            palukanuNorma = palukanuNormaPo;
            if (mokejimoNumeris < mokejimuSkaiciusIki){
                dengiaPaskola = round(menesineImoka - palukanos);
            }else {
                menesineImoka = likoMoketi + palukanos;
                dengiaPaskola = likoMoketi;
            }
            arrayList.add(new Mokejimai(doubleToString(mokejimoNumeris),dateToString(mokejimoData),doubleToString(likoMoketi),
                    doubleToString(dengiaPaskola),doubleToString(palukanos),doubleToString(menesineImoka),palukanuNorma));
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src\\kreditasdu.csv"));
        String[] header = new String[] {"Mok.nr","Data","Liko","Pask.mok.","Pal.mok","Mok.","Pal.n."};
        String head = String.join(";",header) ;
        bufferedWriter.write(head);
        bufferedWriter.newLine();
        for (Mokejimai mokejimai : arrayList){

            bufferedWriter.write(mokejimai.toString());
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
    }
}
