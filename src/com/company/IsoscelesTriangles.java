package com.company;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class IsoscelesTriangles {
    private static double curSquare;
    private static double maxSquare;
    private static String coordsMax = "";

    private static int getPreLength(int a, int b) {
        return (int) Math.pow( a, 2 ) + (int) Math.pow( b, 2 );
    }

    private static double getLineLength(int preLength) {
        return Math.pow( preLength, 0.5 );
    }

    private static double calcSquare(double a, double b) {
        return (b * (Math.pow( Math.pow( a, 2 ) - Math.pow( b, 2 ) / 4, 0.5 ))) / 2;
    }

    private static void evaluate(String statement) {
        String[] arr = statement.split( " " );

        if (arr.length != 6) {
            return;
        }

        int[] res = new int[6];
        for (int i = 0; i < 6; i++) {
            try {
                res[i] = Integer.valueOf( arr[i] );
            } catch (Exception e) {
                return;
            }
        }

        // Не берем корень, чтобы были только целые числа для сравнения линий
        int preLine1 = getPreLength( res[0] - res[1], res[2] - res[3] );
        int preLine2 = getPreLength( res[0] - res[1], res[4] - res[5] );
        int preLine3 = getPreLength( res[2] - res[3], res[4] - res[5] );

        // Проверка на равнобедренность
        if (preLine1 == preLine2 || preLine1 == preLine3 || preLine2 == preLine3) {
            // Если треугольник равнобедненный, то вычисляем длину и площадь
            double line1 = getLineLength( preLine1 );
            double line2 = getLineLength( preLine2 );
            double line3 = getLineLength( preLine3 );

            if (preLine1 == preLine2) {
                curSquare = calcSquare( line1, line3 );
            } else if (preLine1 == preLine3) {
                curSquare = calcSquare( line1, line2 );
            } else {
                curSquare = calcSquare( line2, line1 );
            }

            // Определяем маскимальную площадь в сравнении с текущей
            // Если площадь больше, то обновляем значение
            if (updateMaxSquare()) {
                coordsMax = statement;
            }
        }
    }

    private static boolean updateMaxSquare() {
        if (maxSquare < curSquare) {
            maxSquare = curSquare;
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        // Проверка аргументов командной строки
        if (args.length != 2) {
            System.out.println( "Wrong Arguments count. Usage: IsoscelesTriangles <in.txt> <out.txt>" );
        }

        // Считываем входной файл args[0] построчно
        try (Stream<String> stream = Files.lines( Paths.get( args[0] ) )) {
            stream.forEach( IsoscelesTriangles::evaluate );
        } catch (IOException e) {
            System.out.print( "Failed to read " + args[0]);
            e.printStackTrace();
        }

        // Записываем координаты треугольника с наибольшей площадью в выходной файл args[1]
        try {
            PrintWriter writer = new PrintWriter( args[1], StandardCharsets.UTF_8 );
            writer.println( coordsMax );
            writer.close();
        } catch (IOException e) {
            System.out.print( "Failed to write " + args[1]);
            e.printStackTrace();
        }
    }
}
