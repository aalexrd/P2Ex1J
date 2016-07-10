import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

/**
 * Created by alexs on 7/6/2016.
 */
class Game
{

    private Player player1 = new Player();
    private Player player2 = new Player();
    private String word;
    private StringBuilder input = new StringBuilder(); //default 16 chars
    private Scanner in = new Scanner(System.in);

    private static void cls()
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private boolean letterInput()
    {
        setw("Digite una letra:");
        char letter = getString().toUpperCase().charAt(0);
        int index = word.indexOf(letter);
        if (index >= 0 && input.charAt(index) == letter)
            return true;
        boolean pass = false;
        while (index >= 0)
        {
            pass = true;
            input.setCharAt(index, letter);
            index = word.indexOf(letter, index + 1);
        }
        return pass;
    }

    void UI()
    {
        setw("Bienvenido al juego del ahorcado");
        setw("Ingrese el nombre del primer jugador: ");
        player1.setName(getString());
        setw("Ingrese el nombre del segundo jugador: ");
        player2.setName(getString());
        boolean turn = true; //which player goes, true is first player
        do
        {
            cls();
            input.setLength(0); //clear StringBuilder
            if (turn)
                setw(player1.getName() + " cúal es la palabra que desea que " + player2.getName() + " adivine?");
            else
                setw(player2.getName() + " cúal es la palabra que desea que " + player1.getName() + " adivine?");
            word = in.nextLine().toUpperCase();
            input.setLength(word.length()); //change stringBuilder chars length
            for (int c = 0; c < word.length(); c++) //fill StringBuilder with '_'
                input.setCharAt(c, '_');
            int attempts = 0; //maz of 7
            while (attempts < 7)
            {
                cls();
                if (turn)
                    setw(player2.getName() + " tienes " + (7 - attempts) + " intento/s.");
                else
                    setw(player1.getName() + " tienes " + (7 - attempts) + " intento/s.");
                print(attempts);
                if (!letterInput())
                    ++attempts;
                if (isWinner())
                {
                    if (turn)
                    {
                        setw("Felicidades " + player2.getName() + " has ganado.");
                        player2.setPoints(player2.getPoints() + 1);
                        save(player2); //Save points and name
                    }
                    else
                    {
                        setw("Felicidades " + player1.getName() + " has ganado.");
                        player1.setPoints(player1.getPoints() + 1);
                        save(player1); //Save points and name
                    }
                    in.nextLine();
                    break;
                }
                if (attempts == 7)
                {
                    cls();
                    setw("Has PERDIDO!");
                    print(attempts);
                    in.nextLine();
                }
            }
            turn = !turn; //change turns
        } while (option());
    }

    private void print(int number)
    {
        switch (number)
        {
            case 0:
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();
                break;
            case 1:
                setw("   |   ");
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();
                break;
            case 2:
                setw("   |   ");
                setw("   O   ");
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();
                break;
            case 3:
                setw("   |   ");
                setw("   O   ");
                setw("   |   ");
                setw("   |   ");
                setw("   |   ");
                System.out.println();
                break;
            case 4:
                setw("   |   ");
                setw("   O   ");
                setw("  \\|   ");
                setw("   |   ");
                setw("   |   ");
                System.out.println();
                break;
            case 5:
                setw("   |   ");
                setw("   O   ");
                setw("  \\|/  ");
                setw("   |   ");
                setw("   |   ");
                System.out.println();
                break;
            case 6:
                setw("   |   ");
                setw("   O   ");
                setw("  \\|/  ");
                setw("   |   ");
                setw("   |   ");
                setw("  /    ");
                break;
            case 7:
                setw("   |   ");
                setw("   X   ");
                setw("  \\|/  ");
                setw("   |   ");
                setw("   |   ");
                setw("  / \\  ");
                break;
        }
        System.out.println();
        setw(input.toString());
        System.out.println();
    }

    private boolean isWinner()
    {
        return Objects.equals(word, input.toString());
    }

    private boolean option()
    {
        System.out.println("Desea continuar, SI o NO?: ");
        return getString().toUpperCase().charAt(0) == 'S';
    }

    private void setw(String str)
    {
        int pos = (80 - str.length()) / 2;
        for (int i = 0; i < pos; i++)
            System.out.print(' ');
        System.out.println(str);
    }

    private void save(Player p)
    {
        try (FileWriter file = new FileWriter(p.getName() + ".txt", true))
        {
            file.write("Nombre: " + p.getName() + ", Palabra: " + word + ", Puntaje: " + Integer.toString(p.getPoints()) + "\r\n");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private String getString()
    {
        String line = in.nextLine();
        if (line.isEmpty())
            return getString();
        return line;
    }
}
