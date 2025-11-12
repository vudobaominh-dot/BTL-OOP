import java.util.*;
public class BTL {
    abstract class Person {
        private  String id;
        private  String name;

        protected Person(String id, String name) {
            if (!id.isEmpty()){this.id = id;}
            if (!name.isEmpty()){this.name = name;}
        }
        public String getId()   { return id; }
        public String getName() { return name; }
        public abstract String role();
        @Override public String toString(){ return id + " - " + name; }
    }

    class Student extends Person {
        public Student(String id, String name){ super(id, name); }
        @Override public String role(){ return "SV"; }
    }

    class Course {
        private  String code;
        private  String name;
        private  int credit;

        Course(String code, String name, int credit){
            if (!code.isEmpty()){this.code = code;}
            if (!name.isEmpty()){this.name = name;}
            if (credit != 0 ){this.credit = credit;}
        }
        public String getCode(){ return code; }
        public String getName(){ return name; }
        public int getCredit(){ return credit; }
        @Override public String toString(){ return code + " (" + credit + " TC)"; }
    }

    class Enrollment {
        private String studentId;
        private String courseCode;
        private double score;

        public Enrollment(String studentId, String courseCode, double score) {
            if (!studentId.isEmpty()){this.studentId = studentId;}
            if (!courseCode.isEmpty()){this.courseCode = courseCode;}
            if (score != 0){this.score = score;}
        }
        public String getStudentId(){ return studentId; }
        public String getCourseCode(){ return courseCode; }
        public double getScore(){ return score; }
        public void setScore(double score){
            if (score >= 0 && score <= 10) this.score = score;
        }
    }

    class AvgResult {
        private String id;
        private double weightAvg;
        private double gpa;

        public AvgResult(String id, double weightAvg, double gpa) {
            if (!id.equals("")) {this.id = id;}
            if (weightAvg >= 0.0) {this.weightAvg = weightAvg;}
            if (gpa >= 0.0) {this.gpa = gpa;}
        }
        public String getId(){ return id; }
        public double getWeightAvg(){ return weightAvg; }
        public double getGpa(){ return gpa; }
    }

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

    AvgResult computeResultFor(String studentId,
                               List<Enrollment> enrollments,
                               Map<String, Course> courses) {
        double sumWX = 0, sumW = 0;
        for (Enrollment en : enrollments) {
            if (!en.getStudentId().equals(studentId)) continue;
            Course c = courses.get(en.getCourseCode());
            if (c == null) continue;
            sumWX += en.getScore() * c.getCredit();
            sumW  += c.getCredit();
        }
        double avg10 = 0.0;
        if (sumW > 0) {
            avg10 = sumWX / sumW;
        }
        double gpa4  = GradePolicy.toGPA4(avg10);
        return new AvgResult(studentId, avg10, gpa4);
    }
}
