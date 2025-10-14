class Course {
    private String code;
    private String name;
    private int credit;
    Course(String code, String name, int credit){
        this.code = code;
        this.name = name;
        this.credit = credit;
    }
    @Override
    public String toString(){
        return code + " (" + credit + " TC)";
    }
}