package com.hotelbooking;

import java.sql.*;
import java.util.Scanner;

public class Main {

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Welcome to Hotel Booking System ===");

        while (true) {
            System.out.println("\nSelect role:");
            System.out.println("1. Manager");
            System.out.println("2. Customer");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int roleChoice = sc.nextInt();
            sc.nextLine();

            switch (roleChoice) {
                case 1:
                    if (managerLogin()) managerMenu();
                    else System.out.println("Invalid manager credentials!");
                    break;
                case 2:
                    int customerId = customerLoginOrSignup();
                    if (customerId != -1) customerMenu(customerId);
                    else System.out.println("Login/signup failed!");
                    break;
                case 3:
                    System.out.println("Exiting... Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    // ---------------- Manager ----------------
    private static void managerMenu() {
        while (true) {
            System.out.println("\n=== Manager Menu ===");
            System.out.println("1. Add Room");
            System.out.println("2. Edit Room");
            System.out.println("3. Delete Room");
            System.out.println("4. List All Rooms");
            System.out.println("5. View All Bookings");
            System.out.println("6. Checkout Booking");
            System.out.println("7. Logout");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: addRoom(); break;
                case 2: editRoom(); break;
                case 3: deleteRoom(); break;
                case 4: listAllRooms(); break;
                case 5: viewBookings(); break;
                case 6: managerCheckout(); break;
                case 7: return;
                default: System.out.println("Invalid option!");
            }
        }
    }

    private static void addRoom() {
        System.out.print("Enter room number: ");
        String roomNumber = sc.nextLine();
        System.out.print("Enter type: ");
        String type = sc.nextLine();
        System.out.print("Enter price: ");
        double price = sc.nextDouble();
        sc.nextLine();

        Room room = new Room(roomNumber, type, price);

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO rooms (room_number, type, price) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, room.getRoomNumber());
            ps.setString(2, room.getType());
            ps.setDouble(3, room.getPrice());
            ps.executeUpdate();
            System.out.println("Room added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void editRoom() {
        System.out.print("Enter Room Number to edit: ");
        String roomNumber = sc.nextLine();
        System.out.print("Enter new type: ");
        String type = sc.nextLine();
        System.out.print("Enter new price: ");
        double price = sc.nextDouble();
        sc.nextLine();

        Room room = new Room(roomNumber, type, price);

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE rooms SET type=?, price=? WHERE room_number=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, room.getType());
            ps.setDouble(2, room.getPrice());
            ps.setString(3, room.getRoomNumber());
            int updated = ps.executeUpdate();
            if (updated > 0) System.out.println("Room updated successfully!");
            else System.out.println("Room number not found!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteRoom() {
        System.out.print("Enter Room Number to delete: ");
        String roomNumber = sc.nextLine();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM rooms WHERE room_number=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, roomNumber);
            int deleted = ps.executeUpdate();
            if (deleted > 0) System.out.println("Room deleted successfully!");
            else System.out.println("Room number not found!");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private static void listAllRooms() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT room_number, type, price, is_available FROM rooms ORDER BY room_number";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("=== All Rooms ===");
            while (rs.next()) {
                Room room = new Room();
                room.setRoomNumber(rs.getString("room_number"));
                room.setType(rs.getString("type"));
                room.setPrice(rs.getDouble("price"));
                room.setAvailable(rs.getBoolean("is_available"));

                System.out.printf("Room Number: %s | Type: %s | Price: %.2f | Available: %s%n",
                        room.getRoomNumber(),
                        room.getType(),
                        room.getPrice(),
                        room.isAvailable() ? "Yes" : "No");
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private static void managerCheckout() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT b.booking_id, c.name, b.room_number, b.total_amount FROM bookings b JOIN customers c ON b.customer_id=c.id";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            boolean any = false;
            System.out.println("=== All Bookings ===");
            while (rs.next()) {
                any = true;
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setRoomNumber(rs.getString("room_number"));
                booking.setTotalAmount(rs.getDouble("total_amount"));

                Customer customer = new Customer();
                customer.setName(rs.getString("name"));

                System.out.printf("BookingID: %d | Customer: %s | Room: %s | Total: %.2f%n",
                        booking.getBookingId(), customer.getName(),
                        booking.getRoomNumber(), booking.getTotalAmount());
            }
            if (!any) { System.out.println("No bookings found."); return; }

            System.out.print("Enter Booking ID to checkout: ");
            int bookingId = sc.nextInt(); sc.nextLine();

            String getRoom = "SELECT room_number FROM bookings WHERE booking_id=?";
            PreparedStatement ps2 = conn.prepareStatement(getRoom);
            ps2.setInt(1, bookingId);
            ResultSet rs2 = ps2.executeQuery();

            if (rs2.next()) {
                String roomNumber = rs2.getString("room_number");

                String del = "DELETE FROM bookings WHERE booking_id=?";
                PreparedStatement ps3 = conn.prepareStatement(del);
                ps3.setInt(1, bookingId);
                ps3.executeUpdate();

                String updateRoom = "UPDATE rooms SET is_available=true WHERE room_number=?";
                PreparedStatement ps4 = conn.prepareStatement(updateRoom);
                ps4.setString(1, roomNumber);
                ps4.executeUpdate();

                System.out.println("Checkout completed. Room is now available.");
            } else System.out.println("Booking ID not found!");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // ---------------- Customer ----------------
    private static void customerMenu(int customerId) {
        while (true) {
            System.out.println("\n=== Customer Menu ===");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Book Room");
            System.out.println("3. View My Bookings");
            System.out.println("4. Cancel Booking");
            System.out.println("5. Checkout Booking");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt(); sc.nextLine();

            switch (choice) {
                case 1: listAvailableRooms(); break;
                case 2: bookRoom(customerId); break;
                case 3: viewBookingsForCustomer(customerId); break;
                case 4: cancelBooking(customerId); break;
                case 5: checkoutBooking(customerId); break;
                case 6: return;
                default: System.out.println("Invalid option!");
            }
        }
    }

    private static void listAvailableRooms() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT room_number, type, price FROM rooms WHERE is_available=true ORDER BY room_number";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            boolean any = false;
            System.out.println("=== Available Rooms ===");
            while (rs.next()) {
                any = true;
                Room room = new Room();
                room.setRoomNumber(rs.getString("room_number"));
                room.setType(rs.getString("type"));
                room.setPrice(rs.getDouble("price"));

                System.out.printf("Room Number: %s | Type: %s | Price: %.2f%n",
                        room.getRoomNumber(), room.getType(), room.getPrice());
            }
            if (!any) System.out.println("No rooms available.");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private static void bookRoom(int customerId) {
        try (Connection conn = DBConnection.getConnection()) {
            listAvailableRooms();
            System.out.print("Enter Room Number to book: ");
            String roomNumber = sc.nextLine();
            System.out.print("Enter number of days: ");
            int days = sc.nextInt(); sc.nextLine();

            Room room = null;
            String sql = "SELECT room_number, type, price, is_available FROM rooms WHERE room_number=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, roomNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getBoolean("is_available")) {
                room = new Room(
                        rs.getString("room_number"),
                        rs.getString("type"),
                        rs.getDouble("price")
                );
            }

            if (room != null) {
                double total = room.getPrice() * days;
                Booking booking = new Booking(customerId, room.getRoomNumber(), days, total);

                // Insert booking
                String insert = "INSERT INTO bookings (customer_id, room_number, days, total_amount) VALUES (?, ?, ?, ?)";
                PreparedStatement ps2 = conn.prepareStatement(insert);
                ps2.setInt(1, booking.getCustomerId());
                ps2.setString(2, booking.getRoomNumber());
                ps2.setInt(3, booking.getDays());
                ps2.setDouble(4, booking.getTotalAmount());
                ps2.executeUpdate();

                // Update room availability
                String update = "UPDATE rooms SET is_available=false WHERE room_number=?";
                PreparedStatement ps3 = conn.prepareStatement(update);
                ps3.setString(1, room.getRoomNumber());
                ps3.executeUpdate();

                System.out.println("Room booked successfully! Total: $" + booking.getTotalAmount());
            } else {
                System.out.println("Room not available or invalid!");
            }

        } catch (SQLException e) { e.printStackTrace(); }
    }


    private static void cancelBooking(int customerId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT booking_id, room_number FROM bookings WHERE customer_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            boolean any = false;
            System.out.println("Your bookings:");
            while (rs.next()) {
                any = true;
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setRoomNumber(rs.getString("room_number"));
                System.out.printf("BookingID: %d | Room: %s%n", booking.getBookingId(), booking.getRoomNumber());
            }
            if (!any) { System.out.println("No bookings found."); return; }

            System.out.print("Enter Booking ID to cancel: ");
            int bookingId = sc.nextInt(); sc.nextLine();

            // Get room number for that booking
            sql = "SELECT room_number FROM bookings WHERE booking_id=? AND customer_id=?";
            PreparedStatement ps2 = conn.prepareStatement(sql);
            ps2.setInt(1, bookingId); ps2.setInt(2, customerId);
            ResultSet rs2 = ps2.executeQuery();

            if (rs2.next()) {
                Room room = new Room();
                room.setRoomNumber(rs2.getString("room_number"));

                // Delete booking
                sql = "DELETE FROM bookings WHERE booking_id=?";
                PreparedStatement ps3 = conn.prepareStatement(sql);
                ps3.setInt(1, bookingId);
                ps3.executeUpdate();

                // Make room available again
                sql = "UPDATE rooms SET is_available=true WHERE room_number=?";
                PreparedStatement ps4 = conn.prepareStatement(sql);
                ps4.setString(1, room.getRoomNumber());
                ps4.executeUpdate();

                System.out.println("Booking cancelled and room is now available.");
            } else {
                System.out.println("Booking ID not found!");
            }

        } catch (SQLException e) { e.printStackTrace(); }
    }

    private static void checkoutBooking(int customerId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT booking_id, room_number, total_amount FROM bookings WHERE customer_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            boolean any = false;
            System.out.println("Your bookings:");
            while (rs.next()) {
                any = true;
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setRoomNumber(rs.getString("room_number"));
                booking.setTotalAmount(rs.getDouble("total_amount"));
                System.out.printf("BookingID: %d | Room Number: %s | Total: %.2f%n",
                        booking.getBookingId(), booking.getRoomNumber(), booking.getTotalAmount());
            }
            if (!any) { System.out.println("No bookings found."); return; }

            System.out.print("Enter Booking ID to checkout: ");
            int bookingId = sc.nextInt(); sc.nextLine();

            sql = "SELECT room_number FROM bookings WHERE booking_id=? AND customer_id=?";
            PreparedStatement ps2 = conn.prepareStatement(sql);
            ps2.setInt(1, bookingId); ps2.setInt(2, customerId);
            ResultSet rs2 = ps2.executeQuery();

            if (rs2.next()) {
                Room room = new Room();
                room.setRoomNumber(rs2.getString("room_number"));

                sql = "DELETE FROM bookings WHERE booking_id=?";
                PreparedStatement ps3 = conn.prepareStatement(sql);
                ps3.setInt(1, bookingId);
                ps3.executeUpdate();

                sql = "UPDATE rooms SET is_available=true WHERE room_number=?";
                PreparedStatement ps4 = conn.prepareStatement(sql);
                ps4.setString(1, room.getRoomNumber());
                ps4.executeUpdate();

                System.out.println("Checkout completed. Room is now available.");
            } else {
                System.out.println("Booking ID not found!");
            }

        } catch (SQLException e) { e.printStackTrace(); }
    }

    // ---------------- Login methods ----------------
    private static boolean managerLogin() {
        System.out.print("Enter manager username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM managers WHERE username=? AND password=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    private static int customerLoginOrSignup() {
        System.out.println("1. Login\n2. Signup\nChoose an option: ");
        int choice = sc.nextInt(); sc.nextLine();

        try (Connection conn = DBConnection.getConnection()) {
            if (choice == 1) {
                System.out.print("Enter your email: ");
                String email = sc.nextLine().trim();
                System.out.print("Enter password: ");
                String password = sc.nextLine().trim();
                if(email.isEmpty() || password.isEmpty()) {
                    System.out.println("Email or password cannot be empty!");
                    return -1; // stop login
                }
                String sql = "SELECT id FROM customers WHERE email=? AND password=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, email);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();
                if (rs.next())
                    return rs.getInt("id");
                System.out.println("Invalid email/password!");
            }
            else if (choice == 2) {
                System.out.print("Enter name: "); String name = sc.nextLine();
                System.out.print("Enter phone: "); String phone = sc.nextLine();
                if (!phone.matches("\\d{10}")) {
                    System.out.println("Phone must be exactly 10 digits!");
                    return -1;
                }
                System.out.print("Enter email: "); String email = sc.nextLine();
                System.out.print("Enter password: "); String password = sc.nextLine();
                // Strong password validation
                if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$")) {
                    System.out.println("Password must be at least 8 characters and include uppercase, lowercase, number, and special character!");
                    return -1;
                }

                if (name == null || name.trim().isEmpty() ||
                        email == null || email.trim().isEmpty() ||
                        password == null || password.trim().isEmpty()) {
                    System.out.println("All fields are required!");
                    return -1;
                }

                Customer customer = new Customer(name, Long.parseLong(phone), email);

                String sql = "INSERT INTO customers (name, phone, email, password) VALUES (?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, customer.getName());
                ps.setLong(2, customer.getPhone());
                ps.setString(3, customer.getEmail());
                ps.setString(4, password);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    System.out.println("Signup successful!");
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return -1;
    }

    private static void viewBookings() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT b.booking_id, c.name, b.room_number, b.total_amount, b.booked_at " +
                    "FROM bookings b JOIN customers c ON b.customer_id=c.id";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            boolean any = false;
            System.out.println("=== All Bookings ===");
            while (rs.next()) {
                any = true;
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setRoomNumber(rs.getString("room_number"));
                booking.setTotalAmount(rs.getDouble("total_amount"));
                booking.setBookedAt(rs.getTimestamp("booked_at"));

                Customer customer = new Customer();
                customer.setName(rs.getString("name"));

                System.out.printf("BookingID: %d | Customer: %s | Room: %s | Total: %.2f | At: %s%n",
                        booking.getBookingId(), customer.getName(),
                        booking.getRoomNumber(), booking.getTotalAmount(),
                        booking.getBookedAt());
            }
            if (!any) System.out.println("No bookings found.");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private static void viewBookingsForCustomer(int customerId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT booking_id, room_number, days, total_amount, booked_at FROM bookings WHERE customer_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            boolean any = false;
            System.out.println("=== My Bookings ===");
            while (rs.next()) {
                any = true;
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setRoomNumber(rs.getString("room_number"));
                booking.setDays(rs.getInt("days"));
                booking.setTotalAmount(rs.getDouble("total_amount"));
                booking.setBookedAt(rs.getTimestamp("booked_at"));

                System.out.printf("BookingID: %d | Room: %s | Days: %d | Total: %.2f | At: %s%n",
                        booking.getBookingId(),
                        booking.getRoomNumber(),
                        booking.getDays(),
                        booking.getTotalAmount(),
                        booking.getBookedAt());
            }
            if (!any) System.out.println("No bookings found.");
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
