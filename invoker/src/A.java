/**
 * Created with IntelliJ IDEA.
 * User: blackhawk
 * Date: 04.05.13
 * Time: 0:48
 */
public class A {

    public void test(String a, String b, String c, String d, String e) {
        System.out.println(5);
        System.out.println(a + " " + b + " " + c + " " + d + " " + e);
    }

    public void test(String a, String b, String... c) {
        System.out.println(6);
        System.out.print(a + " " + b);
        for (String s : c) {
            System.out.print(" " + s);
            System.out.println();
        }
    }
}
