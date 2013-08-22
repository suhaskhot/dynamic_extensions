grammar AQL;

query         : SELECT select_list WHERE filter_expr #QueryExpr
              ;
      
select_list   : arith_expr (',' arith_expr)*         #SelectExpr
              ;

filter_expr   : filter_expr AND filter_expr          #AndFilterExpr
              | filter_expr OR filter_expr           #OrFilterExpr
              | LP filter_expr RP                    #ParensFilterExpr
              | NOT filter_expr                      #NotFilterExpr
              | filter                               #SimpleFilter
              ;
     
filter        : arith_expr OP arith_expr
              ;
	 
arith_expr    : arith_expr ARITH_OP arith_expr       #ArithExpr
              | arith_expr ARITH_OP date_interval    #DateIntervalExpr
              | LP arith_expr RP                     #ParensArithExpr
              | MONTHS LP arith_expr RP              #MonthsDiffFunc
              | YEARS LP arith_expr RP               #YearsDiffFunc
              | FIELD                                #Field
              | SLITERAL                             #StringLiteral
              | INT                                  #IntLiteral
              | FLOAT                                #FloatLiteral
              ;	 
          
date_interval : YEAR MONTH? DAY?
              | YEAR? MONTH DAY?
              | YEAR? MONTH? DAY
              ;          
                   
               
WS       : [ \t\n\r]+ -> skip;

SELECT   : 'select';
WHERE    : 'where';
MONTHS   : 'months';
YEARS    : 'years';
OR       : 'or';
AND      : 'and';
NOT      : 'not';
LP       : '(';
RP       : ')';

FIELD    : (INT|ID) '.' ID ('.' ID)*;
INT      : '-'? DIGIT+;
FLOAT    : '-'? DIGIT+ '.' DIGIT+;
YEAR     : DIGIT+ ('y'|'Y');
MONTH    : DIGIT+ ('m'|'M');
DAY      : DIGIT+ ('d'|'D');
DIGIT    : ('0'..'9');
SLITERAL : '"' SGUTS '"';
ESC      : '\\' ('\\' | '"');
ID       : ('a'..'z'|'A'..'Z'|'_')('a'..'z'|'A'..'Z'|'0'..'9'|'_')*;
OP       : ('>'|'<'|'>='|'<='|'='|'!='|'like');
ARITH_OP : ('+'|'-'|'*'|'/');

fragment
SGUTS    : (ESC | ~('\\' | '"'))*;
QUOTE    : '"';
