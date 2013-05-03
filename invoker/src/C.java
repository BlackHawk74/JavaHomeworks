/**
 * Created with IntelliJ IDEA.
 * User: blackhawk
 * Date: 04.05.13
 * Time: 0:48
 */
public class C extends B {

    public void test(String a, String b, String c, String d, String e) {
        System.out.println(1);
        System.out.println(a + " " + b + " " + c + " " + d + " " + e);
    }

    public void test(String a, String b, String... c) {
        System.out.println(2);
        System.out.print(a + " " + b);
        for (String s : c) {
            System.out.print(" " + s);
        }
        System.out.println();
    }

    private void test(String a, String... b) {
        System.out.println(8);
        System.out.print(a);
        for (String s : b) {
            System.out.print(" " + s);
        }
        System.out.println();
    }

    public void test(CharSequence a, CharSequence b, CharSequence c, CharSequence d) {
        System.out.println(9);
        System.out.println(a + " " + b + " " + c + " " + d);
    }

    public void test(String a, CharSequence... args) {
        System.out.println(10);
        System.out.print(a);
        for (CharSequence s : args) {
            System.out.print(" " + s);
        }
        System.out.println();
    }

    private void test(String a, String b, String c, String... args) {
        System.out.println(12);
        System.out.print(a + " " + b + " " + c);
        for (String s : args) {
            System.out.print(" " + s);
        }
        System.out.println();
    }
}
