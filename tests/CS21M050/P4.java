/*
* Author : Ravindra kumar
* Last modified: 10-March-2022
* Bugs: None
 */
class P4{

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
		int a;
		int b;
		boolean res;
		boolean flag;
		f1 = new B();
		x.f0 = f1;
		a = 10;
		b = 5;
		flag = a < b;
		if(flag){
			res = x.foo(x);
		}
		else{
			res = false;
		}
		ret = true;
		return ret;
	}
	public A getf1(){
		return f1;
	}

}
