import java.util.Scanner;
import java.time.LocalDate;

public class TestPayment {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        // --- SECTION 1: TICKET SELECTION (from TestTicket) ---
        Ticket t1 = new Ticket();
   
        int tickets = -1;

		do {
    		System.out.print(Ticket.ticketHeader1());
    		System.out.print(Ticket.noOfTickets());

    		if (scan.hasNextInt()) {
        		tickets = scan.nextInt();
        		scan.nextLine(); // Clear buffer
    		} else {
        		System.out.println("Invalid input. Please enter a number.");
        		scan.nextLine(); // Discard incorrect input
    		}

		} while (tickets <= 0);

			Ticket[] tic = new Ticket[tickets];
    	
       
        int size = -1;
        System.out.print("\n ======================");
        System.out.print("\n|       Hall Size      |");
        System.out.print("\n ======================");
        do {
            System.out.print(Ticket.selectHallSize());
            
            if(scan.hasNextInt()){
            	size = scan.nextInt();
            	scan.nextLine();//clear buffer
            }
            else {
            	System.out.print(" ");
            	scan.nextLine();// discard incorrect input
            }
     		if(size < 1 || size > 3){
     			System.out.println(Ticket.sizeInvalidMsg());
     		}
        } while (size < 1 || size > 3);
        
        for (int i = 0; i < tickets; i++) {
    		String ticketType = "";
    		int choice = 0;        // initialize to 0 so do-while starts correctly
    		double ticketAmt = 0;

    		do {
        		switch (size) {
            		case 1:
                		System.out.print(Ticket.selectTicketType1() + (i + 1) + ": ");
                		break;
            		case 2:
                		System.out.print(Ticket.selectTicketType2() + (i + 1) + ": ");
               			break;
            		case 3:
                		System.out.print(Ticket.selectTicketType3() + (i + 1) + ": ");
                		break;
        		}

        		//Validate input before reading - catches letters/symbols
        		if (scan.hasNextInt()) {
            		choice = scan.nextInt();
            		scan.nextLine(); // clear buffer
        		} else {
            		System.out.println(Ticket.invalidType());
            		scan.nextLine(); // discard bad input
            		continue;        // restart loop without touching choice
        		}

        		switch (choice) {
            		case 1: ticketType = "Adult";  ticketAmt = (size == 1) ? 15.00 : (size == 2) ? 20.00 : 25.00; break;
            		case 2: ticketType = "Child";  ticketAmt = (size == 1) ? 10.00 : (size == 2) ? 12.00 : 15.00; break;
            		case 3: ticketType = "Senior"; ticketAmt = (size == 1) ? 10.00 : (size == 2) ? 12.00 : 15.00; break;
            		default: System.out.println(Ticket.invalidType()); break;
        		}

    		} while (choice < 1 || choice > 3);

    		tic[i] = new Ticket(ticketType, ticketAmt);
		}
        
        // Print Ticket Summary
        System.out.println(Ticket.ticketHeader2());
        for (Ticket t : tic) {
            System.out.println(t);
        }
        System.out.println(Ticket.printTotal(tic));	
        System.out.println("\n--- Proceeding to Payment ---");

        // --- SECTION 2: PAYMENT METHOD (from TestPaymentMethod) ---
        PaymentMethod pm = new PaymentMethod();
        CreditCardPayment card = new CreditCardPayment();
        EWalletPayment wallet = new EWalletPayment();
        
        int payMethod = 0;
        String cardHolder, cardNumber, walletOwner, walletAccID;
        int cardCvc = 0, expireMonth = 0, expireYear = 0, walletType = 0;
        
        System.out.println(PaymentMethod.paymentMethodHeader());
        do {
            System.out.print(pm.toString());
            String methodInput = scan.nextLine();
            if(!methodInput.matches("\\d+")) {
                System.out.println("Invalid payment method! Must enter digit only.\n");
                continue;
            }
            payMethod = Integer.parseInt(methodInput);
            pm.setMethodType(payMethod);	
            System.out.println(PaymentMethod.validatePayMethod(payMethod));
        } while(payMethod < 1 || payMethod > 2);
        
        switch(payMethod) {
            case 1:
                LocalDate today = LocalDate.now();
                int currentMonth = today.getMonthValue();
                int currentYear = today.getYear() % 100;
                System.out.println(CreditCardPayment.creditCardHeader());
                do {
                    do {
                        System.out.print("Enter card holder name: ");
                        cardHolder = scan.nextLine().trim();
                        card.setCardHolder(cardHolder);
                        System.out.println(card.validateCardHolder());
                    } while(cardHolder.isEmpty() || !cardHolder.matches("[A-Z ]+"));
                    
                    do {
                        System.out.print("Enter card number (e.g. xxxx xxxx xxxx xxxx): ");
                        cardNumber = scan.nextLine().trim();
                        card.setCardNumber(cardNumber);
                        System.out.println(card.validateCardNum());
                    } while(!cardNumber.matches("\\d{4} \\d{4} \\d{4} \\d{4}"));
                    
                    do {
                        System.out.print("Enter card cvc (100-9999): ");
                        String cvcInput = scan.nextLine().trim();
                        if(!cvcInput.matches("\\d+")) {
                            System.out.println("Invalid card cvc! Must contain digits only.");
                            continue;
                        }
                        cardCvc = Integer.parseInt(cvcInput);
                        card.setCardCvc(cardCvc);
                        System.out.println(card.validateCardCvc());
                    } while(cardCvc < 100 || cardCvc > 9999);
                    
                    do {
                        System.out.print("Enter expiry month (e.g. 06): ");
                        String monthInput = scan.nextLine();
                        if(!monthInput.matches("\\d{2}+")) {
                            System.out.println("Invalid month! Must contain 2 digits only.");
                            continue;
                        }
                        expireMonth = Integer.parseInt(monthInput);
                        card.setExpireMonth(expireMonth);
                        System.out.println(card.validateMonth());
                    } while(expireMonth < 1 || expireMonth > 12);
                    
                    do {
                        System.out.print("Enter expiry year within 10 years (e.g. 28): ");
                        String yearInput = scan.nextLine();
                        if(!yearInput.matches("\\d+")) {
                            System.out.println("Invalid year! Must contain digits only.");
                            continue;
                        }
                        expireYear = Integer.parseInt(yearInput);
                        card.setExpireYear(expireYear);
                        System.out.println(card.validateYear());
                    } while(expireYear < currentYear || expireYear > currentYear + 10);
                    
                    System.out.println(card.validateExpire());
                } while(expireYear == currentYear && expireMonth < currentMonth);
                System.out.println(card.toString());
                break;		
        
            case 2:
            	System.out.println(EWalletPayment.eWalletHeader());
                do {
                    System.out.print(wallet.eWalletType());
                    String typeInput = scan.nextLine();
                    if(!typeInput.matches("\\d")) {
                        System.out.println("Invalid e-wallet type! Please enter again.\n");
                        continue;
                    }
                    walletType = Integer.parseInt(typeInput);
                    wallet.setWalletType(walletType);
                    System.out.println(wallet.validateEWalletType(walletType));
                } while(walletType < 1 || walletType > 3);
                
                do {
                    System.out.print("Enter wallet owner name: ");
                    walletOwner = scan.nextLine().trim();
                    wallet.setWalletOwner(walletOwner);
                    System.out.println(wallet.validateWalletOwner());
                } while(walletOwner.isEmpty() || !walletOwner.matches("[A-Z ]+"));
                
                do {
                    System.out.print("Enter wallet account ID (e.g. 0123456789): ");
                    walletAccID = scan.nextLine();
                    wallet.setWalletAccID(walletAccID);
                    System.out.println(wallet.validateWalletAccID());
                } while(walletAccID.trim().isEmpty()
                        || !walletAccID.matches("\\d+")
                        || !walletAccID.startsWith("01")
                        || (walletAccID.startsWith("010") || walletAccID.startsWith("011"))
                        && walletAccID.length() != 11
                        || (!walletAccID.startsWith("010") && !walletAccID.startsWith("011"))
                        && walletAccID.length() != 10);
                System.out.println(wallet.toString());
                break;
        }

        // --- SECTION 3: FINAL PAYMENT CONFIRMATION (from TestPayment) ---
        Payment pay = new Payment();
        // FoodOrder fo = new FoodOrder(); // Included if needed for summary
        pay.setSubTotal(t1.calculateTicketTotal(tic));
        
        
        System.out.println("\n" + Payment.paymentHeader());
        System.out.println(pay.toString());
        
        String confirm;
        do {
            System.out.print(Payment.confirmationMsg());
            confirm = scan.nextLine().trim().toUpperCase();
            
            if(!confirm.equals("Y") && !confirm.equals("N")) {
                System.out.println(Payment.invalidConfirmMsg());
            }
        } while(!confirm.equals("Y") && !confirm.equals("N"));
        
        if(confirm.equals("Y")) {
            System.out.println(Payment.confirmMsg());
        } else {
            System.out.println(Payment.cancelledMsg());
        }
    }
}
