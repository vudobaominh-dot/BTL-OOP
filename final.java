import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

// 1. MODEL
class Student implements Serializable {
    String id, name;
    public Student(String id, String name) { this.id = id; this.name = name; }
    @Override public String toString() { return id + " - " + name; }
    @Override public boolean equals(Object o) { if(o instanceof Student) return id.equals(((Student)o).id); return false; }
}

class Course {
    String name; int credit;
    public Course(String n, int c) {name=n; credit=c; }
    @Override public String toString() { return name; }
}

class Enrollment {
    String sid, cid; double score;
    public Enrollment(String s, String c, double sc) { sid=s; cid=c; score=sc; }
}

// Class Logic Tính điểm (Theo yêu cầu mới)
class GradePolicy {
    static double toGPA4(double a){
        if (a >= 9.0) return 4.0;
        if (a >= 8.5) return 3.7;
        if (a >= 8.0) return 3.5;
        if (a >= 7.0) return 3.0;
        if (a >= 6.5) return 2.5;
        if (a >= 5.5) return 2.0;
        if (a >= 5.0) return 1.5;
        if (a >= 4.0) return 1.0;
        return 0.0;
    }
    static String letterFromGpa(double g){
        if (g == 4.0) return "A+";
        if (g >= 3.7) return "A";
        if (g >= 3.5) return "B+";
        if (g >= 3.0) return "B";
        if (g >= 2.5) return "C+";
        if (g >= 2.0) return "C";
        if (g >= 1.5) return "D+";
        if (g >= 1.0) return "D";
        return "F";
    }
}

// 2. APP
public class BTLOOP extends JFrame {
    List<Student> students = new ArrayList<>();
    List<Enrollment> enrollments = new ArrayList<>();
    List<Course> courses = Arrays.asList(new Course("Lập trình hướng đối tượng",3),
            new Course("Cơ sở dữ liệu", 3),
            new Course("Hệ điều hành", 3),
            new Course("Mạng máy tính", 3),
            new Course("Cơ sở an toàn thông tin", 3),
            new Course("Kinh tế chính trị Mác-Lênin", 2));

    DefaultTableModel tmStd = new DefaultTableModel(new String[]{"Mã SV", "Tên"}, 0);
    DefaultTableModel tmGrd = new DefaultTableModel(new String[]{"Mã SV", "Môn", "Điểm"}, 0);
    DefaultTableModel tmSum = new DefaultTableModel(new String[]{"Mã SV", "Tên", "TB (10)", "GPA (4)", "Xếp loại"}, 0);

    JComboBox<Student> cbStd = new JComboBox<>();
    JComboBox<Course> cbCrs = new JComboBox<>();
    JTable tblStd, tblGrd;

    public BTLOOP() {
        loadData();
        setTitle("App Điểm Sinh Viên"); setSize(850, 500); setDefaultCloseOperation(EXIT_ON_CLOSE);
        JTabbedPane tabs = new JTabbedPane();

        // TAB 1: SINH VIÊN
        JPanel p1 = new JPanel(new BorderLayout());
        JPanel p1In = new JPanel();

        JTextField tfId = new JTextField(10);
        JTextField tfName = new JTextField(15);
        JButton btnAddStd = new JButton("Thêm"), btnDelStd = new JButton("Xóa SV");

        p1In.add(new JLabel("MSV:")); p1In.add(tfId); p1In.add(new JLabel("Tên:")); p1In.add(tfName);
        p1In.add(btnAddStd); p1In.add(btnDelStd);

        tblStd = new JTable(tmStd);
        p1.add(new JScrollPane(tblStd), "Center"); p1.add(p1In, "South");

        // TAB 2: ĐIỂM
        JPanel p2 = new JPanel(new BorderLayout());
        JPanel p2In = new JPanel();
        JTextField tfSc = new JTextField(5);
        for(Course c : courses) cbCrs.addItem(c);
        JButton btnAddGrd = new JButton("Lưu Điểm"), btnDelGrd = new JButton("Xóa Điểm");

        p2In.add(cbStd); p2In.add(cbCrs); p2In.add(new JLabel("Điểm:")); p2In.add(tfSc);
        p2In.add(btnAddGrd); p2In.add(btnDelGrd);

        tblGrd = new JTable(tmGrd);
        p2.add(new JScrollPane(tblGrd), "Center"); p2.add(p2In, "North");

        // TAB 3: TỔNG KẾT
        tabs.addTab("SV", p1); tabs.addTab("Điểm", p2); tabs.addTab("GPA", new JScrollPane(new JTable(tmSum)));
        add(tabs);

        // --- SỰ KIỆN ---
        btnAddStd.addActionListener(e -> {
            if(tfId.getText().isEmpty()) return;
            Student s = new Student(tfId.getText(), tfName.getText());
            students.removeIf(old -> old.id.equals(s.id));
            students.add(s);
            refreshStdTable();
            saveAllStudents();
        });

        btnDelStd.addActionListener(e -> {
            int row = tblStd.getSelectedRow();
            if (row >= 0) {
                String id = (String) tmStd.getValueAt(row, 0);
                students.removeIf(s -> s.id.equals(id));
                enrollments.removeIf(en -> en.sid.equals(id));
                refreshStdTable();
                refreshGrdTable();
                saveAllStudents();
                saveAllGrades();
            }
        });

        btnAddGrd.addActionListener(e -> {
            try {
                Student s = (Student) cbStd.getSelectedItem();
                Course c = (Course) cbCrs.getSelectedItem();
                double sc = Double.parseDouble(tfSc.getText());
                if(s == null) return;
                enrollments.removeIf(x -> x.sid.equals(s.id) && x.cid.equals(c.name));
                enrollments.add(new Enrollment(s.id, c.name, sc));
                refreshGrdTable();
                saveAllGrades();
            } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Điểm lỗi!"); }
        });

        btnDelGrd.addActionListener(e -> {
            int row = tblGrd.getSelectedRow();
            if (row >= 0) {
                String sid = students.stream().filter(s -> s.name.equals(tmGrd.getValueAt(row, 0))).findFirst().get().id;
                String cid = (String) tmGrd.getValueAt(row, 1);
                enrollments.removeIf(x -> x.sid.equals(sid) && x.cid.equals(cid));
                refreshGrdTable();
                saveAllGrades();
            }
        });

        // Logic tính GPA theo GradePolicy
        tabs.addChangeListener(e -> {
            tmSum.setRowCount(0);
            for(Student s : students) {
                double tu = 0, mau = 0;
                for(Enrollment en : enrollments) {
                    if(en.sid.equals(s.id)) {
                        int cre = courses.stream().filter(c->c.name.equals(en.cid)).findFirst().get().credit;
                        tu += en.score * cre; mau += cre;
                    }
                }
                double avg10 = (mau==0) ? 0 : tu/mau;

                // Quy doi diem sang thang 4.0 va thang diem chu A
                double gpa4 = GradePolicy.toGPA4(avg10);
                String rank = GradePolicy.letterFromGpa(gpa4);

                tmSum.addRow(new Object[]{
                        s.id,
                        s.name,
                        String.format("%.2f", avg10),
                        gpa4,
                        rank
                });
            }
        });
    }
    // --- HELPER FUNCTIONS ---
    void refreshStdTable() {
        tmStd.setRowCount(0); cbStd.removeAllItems();
        for(Student s : students) { tmStd.addRow(new Object[]{s.id, s.name}); cbStd.addItem(s); }
    }
    void refreshGrdTable() {
        tmGrd.setRowCount(0);
        for(Enrollment e : enrollments) {
            String sName = students.stream().filter(s->s.id.equals(e.sid)).findFirst().orElse(new Student("","Unknown")).name;
            tmGrd.addRow(new Object[]{sName, e.cid, e.score});
        }
    }
    void saveAllStudents() {
        try (PrintWriter pw = new PrintWriter("student.txt")) {
            for(Student s : students) pw.println(s.id + "," + s.name);
        } catch (Exception e) {}
    }
    void saveAllGrades() {
        try (PrintWriter pw = new PrintWriter("grade.txt")) {
            for(Enrollment e : enrollments) pw.println(e.sid + "," + e.cid + "," + e.score);
        } catch (Exception e) {}
    }
    void loadData() {
        try {
            Scanner sc = new Scanner(new File("student.txt"));
            while(sc.hasNextLine()) { String[] p = sc.nextLine().split(","); students.add(new Student(p[0], p[1])); }
            refreshStdTable();
            sc = new Scanner(new File("grade.txt"));
            while(sc.hasNextLine()) { String[] p = sc.nextLine().split(","); enrollments.add(new Enrollment(p[0], p[1], Double.parseDouble(p[2]))); }
            refreshGrdTable();
        } catch (Exception e) {}
    }

    public static void main(String[] args) { new BTLOOP().setVisible(true); }
}
