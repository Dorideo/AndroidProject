package com.example.project.Calendar;

public class Calendar {

        public static int[] daysInMonths = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        public static boolean isALeapYear(int year){
            if(year%4 == 0){
                if(year%100 == 0){
                    if(year%400 == 0){
                        return true;
                    }
                    return false;
                }
                return true;
            }
            return false;
        }

        public static int getNumberOfLeapYears(int year1, int year2){
            int res = 0;
            for(int i = year1; i <= year2; i++){
                if(isALeapYear(i)){
                    res++;
                }
            }
            return res;
        }

        public static int getFirstDayOfAYear(int year){
            //The first day in 1 A.D. according to google is Saturday
            int firstDay = 6;
            int leapYears = getNumberOfLeapYears(0, year);

            firstDay += year + leapYears + (isALeapYear(year)?-1:0);

            firstDay = firstDay%7;
            return firstDay;
        }

        public static int getDayOfAYear(int day, int month,int year){
            if(isALeapYear(year)){
                daysInMonths[1] += 1;
            }

            int res = 0;

            for(int i = 0; i < month; i++){
                res += daysInMonths[i];
            }


            res += day;

            if(isALeapYear(year))
                daysInMonths[1] -= 1;


            return res;
        }

        public static int getDayOfAWeek(int day, int month, int year){
            if(isALeapYear(year)){
                daysInMonths[1] += 1;
            }

            int res = getFirstDayOfAYear(year);

            for(int i = 0; i < month; i++){
                res += daysInMonths[i];
            }


            res += day-1;//day-1 because here the Sunday represents 0 not 7;


            if(isALeapYear(year)){
                daysInMonths[1] -= 1;
            }

            return res%7;
        }
}

