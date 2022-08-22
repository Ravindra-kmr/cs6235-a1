/*
* Author : Ravindra kumar
* Last modified: 10-March-2022
* Bugs: None
 */
class P5{

	public static void main(String[] args){
		B a1;
		A a2;
		A a3;
		boolean res;
		a1 = new B();
		res = a1.foo(a1);
		a2 = a1.getf0();
		a3 = a1.getf1();
	}
}
class A {
	A f0;
	public boolean foo(A x){
		boolean ret;
		f0 = new A();
		x.f0 = f0;
		ret = true;
		return ret;
	}
	public A getf0(){
		return f0;
	}
}
class B extends A{
	B f1;
	public boolean foo(A x){
		boolean ret;
		boolean flag;
		B b1;
		A a5;
		f1 = new B();
		b1 = this;
		x.f0 = f1;
		a5 = b1.getf0();
		ret = true;
		return ret;
	}
	public A getf1(){
		return f1;
	}
}
