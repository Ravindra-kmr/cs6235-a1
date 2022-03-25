//To check context insensitivity. No call graph needed. Simple Alias check example.
/*
* Author : Ravindra kumar
* Last modified: 10-March-2022
* Bugs: None
 */
class P1{
	public static void main(String[] args){
		A obj1;
		A obj2;
		A obj3;
		A obj4;
		A obj5;
		obj1 = new A();
		obj2 = new A();
		obj3 = new A();
		obj1 = obj2;
		obj2.f1 = obj3;
		obj4 = obj1.getf1();
		obj5 = obj2.getf1();
	}
}
class A{
	A f1;
	public boolean foo(){
		boolean a;
		a = true;
		return a;
	}
	public A getf1(){
		return f1;
	}
}
