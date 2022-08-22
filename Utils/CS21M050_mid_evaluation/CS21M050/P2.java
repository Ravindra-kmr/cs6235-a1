/*
* Author : Ravindra kumar
* Last modified: 10-March-2022
* Bugs: None
 */
class P2 {
    public static void main (String [] args) {
      A a;
      F temp;
      F p;
      F q;
      boolean res;

      a = new A();
      temp = new F();
      
      a.f=temp;
      p = temp;
      q=p.foo();
      res = a.bar(q);
    }
  } 

class F {
  public F foo() {
    F temp;
    temp = this;
    return temp;
  }
}

class A {
  F f;
  public boolean bar(F x) {
    F y;
    boolean a;
    y = f;
    a = true;
    return a;
  }
}

