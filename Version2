public class BTL {
    class Student {
        private String id;
        private String name;

        public Student(String id, String name){
            if (!id.isEmpty()){this.id = id;}
            if (!name.isEmpty()){this.name = name;}
        }
        @Override
        public String toString(){
            return id + " - " + name;
        }
    }
    class Course {
        private String code;
        private String name;
        private int credit;
        Course(String code, String name, int credit){
            if (!code.isEmpty()){this.code = code;}
            if (!name.isEmpty()){this.name = name;}
            if (credit != 0 ){this.credit = credit;}
        }
        @Override
        public String toString(){
            return code + " (" + credit + " TC)";
        }
    }
    class Enrollment {
        private String studentId;
        private String courceCode;
        private double score;
        public Enrollment(String studentId, String courceCode, double score) {
            if (!studentId.isEmpty()){this.studentId = studentId;}
            if (!courceCode.isEmpty()){this.courceCode = courceCode;}
            if (score != 0){this.score = score;}
        }
    }
    class AvgResult {
        private String id;
        private double weightAvg;
        private double gpa;
        public AvgResult(String id, double weightAvg, double gpa) {
            if (!id.equals("")) {this.id = id;}
            if (weightAvg != 0.0) {this.weightAvg = weightAvg;}
            if (gpa != 0.0) {this.gpa = gpa;}
        }
    }
}
