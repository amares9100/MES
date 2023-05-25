


use MES;

-- company
-- 1번 제조처 2번 판매처 (Company)
insert into company (cname, ctype) values ("삼성", 1 );
insert into company (cname, ctype) values ("이젠", 2 );

-- member
insert into member(position, mname, mpassword,  cno) value("사원", "가나다",'qwe123' , 1);
insert into member(position, mname, mpassword, cno) value("대리", "마바사", 'asd123', 1);
insert into member(position, mname, mpassword, cno) value("전무", "ABC",'abc123', 1);

insert into member(position, mname, mpassword, cno) value("사원", "DEF",'def123', 2);
insert into member(position, mname, mpassword, cno) value("대리", "아자차",'cvb123', 2);
insert into member(position, mname, mpassword,  cno) value("전무", "카타파",'rty123', 2);

--

-- material

insert into material(mat_code, mat_name, mat_price, mat_st_exp, mat_unit, cno) values ('A', '물' , 1000 , '2025-05-03' , 'ml' , 1);
insert into material(mat_code, mat_name, mat_price, mat_st_exp, mat_unit, cno) values ('B', '패트' , 1500 , '2025-05-04' , 'g' , 1);
insert into material(mat_code, mat_name, mat_price, mat_st_exp, mat_unit, cno) values ('A', '원두' , 10000 , '2025-06-03' , 'g' , 1);
insert into material(mat_code, mat_name, mat_price, mat_st_exp, mat_unit, cno) values ('B', '상자' , 1300 , '0000-00-00' , 'g' , 1);
insert into material(mat_code, mat_name, mat_price, mat_st_exp, mat_unit, cno) values ('A', '우유' , 3200 , '2025-05-03' , 'ml' , 1);
insert into material(mat_code, mat_name, mat_price, mat_st_exp, mat_unit, cno) values ('A', '시럽' , 5400 , '2025-05-04' , 'g' , 1);

