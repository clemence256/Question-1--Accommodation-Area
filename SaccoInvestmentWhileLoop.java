import java.util.Scanner;

public class SaccoInvestmentWhileLoop {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        final double INTEREST_RATE = 0.05;

        System.out.print("Enter the number of members: ");
        int numMembers = input.nextInt();

        int i = 1;
        while (i <= numMembers) {
            System.out.println("\nMember " + i);
            System.out.print("Enter principal amount: ");
            double principal = input.nextDouble();

            System.out.print("Enter time in years: ");
            int time = input.nextInt();

            double investmentValue = principal + (principal * INTEREST_RATE * time);
            System.out.println("Value after " + time + " years = " + investmentValue);
            i++;
        }

        input.close();
    }
}
