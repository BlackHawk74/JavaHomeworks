/**
 * Created with IntelliJ IDEA.
 * User: blackhawk
 * Date: 04.05.13
 * Time: 0:48
 */
public class B extends A {

    public void test(String a, String b, String c, String d, String e) {
        System.out.println(3);
        System.out.println(a + " " + b + " " + c + " " + d + " " + e);
    }

    public void test(String a, String b, String... c) {
        System.out.println(4);
        System.out.print(a + " " + b);
        for (String s : c) {
            System.out.print(" " + s);
            System.out.println();
        }
    }

    protected void test(String... args) {
        System.out.println(7);
        boolean f = false;
        for (String s : args) {
            System.out.print((!f ? "" : " ") + s);
            f = true;
        }
        System.out.println();
    }

    private void test(String a, String b, String c, String... args) {
        System.out.println(2);
        System.out.print(a + " " + b + " " + c);
        for (String s : args) {
            System.out.print(" " + s);
        }
        System.out.println();
    }

}
