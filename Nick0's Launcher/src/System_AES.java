public class System_AES{private static final int a=32;private byte[][]b;private byte[][] c;private static final byte[]d={99,124,119,123,-14,107,111,-59,48,1,103,43,-2,-41,-85,118,-54,-126,-55,125,-6,89,71,-16,-83,-44,-94,-81,-100,-92,114,-64,-73,-3,-109,38,54,63,-9,-52,52,-91,-27,-15,113,-40,49,21,4,-57,35,-61,24,-106,5,-102,7,18,-128,-30,-21,39,-78,117,9,-125,44,26,27,110,90,-96,82,59,-42,-77,41,-29,47,-124,83,-47,0,-19,32,-4,-79,91,106,-53,-66,57,74,76,88,-49,-48,-17,-86,-5,67,77,51,-123,69,-7,2,127,80,60,-97,-88,81,-93,64,-113,-110,-99,56,-11,-68,-74,-38,33,16,-1,-13,-46,-51,12,19,-20,95,-105,68,23,-60,-89,126,61,100,93,25,115,96,-127,79,-36,34,42,-112,-120,70,-18,-72,20,-34,94,11,-37,-32,50,58,10,73,6,36,92,-62,-45,-84,98,-111,-107,-28,121,-25,-56,55,109,-115,-43,78,-87,108,86,-12,-22,101,122,-82,8,-70,120,37,46,28,-90,-76,-58,-24,-35,116,31,75,-67,-117,-118,112,62,-75,102,72,3,-10,14,97,53,87,-71,-122,-63,29,-98,-31,-8,-104,17,105,-39,-114,-108,-101,30,-121,-23,-50,85,40,-33,-116,-95,-119,13,-65,-26,66,104,65,-103,45,15,-80,84,-69,22};private static final byte[] e={82,9,106,-43,48,54,-91,56,-65,64,-93,-98,-127,-13,-41,-5,124,-29,57,-126,-101,47,-1,-121,52,-114,67,68,-60,-34,-23,-53,84,123,-108,50,-90,-62,35,61,-18,76,-107,11,66,-6,-61,78,8,46,-95,102,40,-39,36,-78,118,91,-94,73,109,-117,-47,37,114,-8,-10,100,-122,104,-104,22,-44,-92,92,-52,93,101,-74,-110,108,112,72,80,-3,-19,-71,-38,94,21,70,87,-89,-115,-99,-124,-112,-40,-85,0,-116,-68,-45,10,-9,-28,88,5,-72,-77,69,6,-48,44,30,-113,-54,63,15,2,-63,-81,-67,3,1,19,-118,107,58,-111,17,65,79,103,-36,-22,-105,-14,-49,-50,-16,-76,-26,115,-106,-84,116,34,-25,-83,53,-123,-30,-7,55,-24,28,117,-33,110,71,-15,26,113,29,41,-59,-119,111,-73,98,14,-86,24,-66,27,-4,86,62,75,-58,-46,121,32,-102,-37,-64,-2,120,-51,90,-12,31,-35,-88,51,-120,7,-57,49,-79,18,16,89,39,-128,-20,95,96,81,127,-87,25,-75,74,13,45,-27,122,-97,-109,-55,-100,-17,-96,-32,59,77,-82,42,-11,-80,-56,-21,-69,60,-125,83,-103,97,23,43,4,126,-70,119,-42,38,-31,105,20,99,85,33,12,125};private static final byte[] f={0,1,2,4,8,16,32,64,-128,27,54,108,-40,-85,77,-102,47,94,-68,99,-58,-105,53,106,-44,-77,125,-6,-17,-59,-111};private static final int g=4;private static final int h=16;private static final int i=0x11B;private static final int j=h/g;private static final int[] k={0,1,2,3};private static final int[] l=new int[256];private static final int[] m=new int[256];static{int i, j;l[0]=1;for(i=1;i<256;i++){j=(l[i-1]<<1)^l[i-1];if((j&0x100)!=0){j^=System_AES.i;}l[i]=j;}for(i=1;i<255;i++){m[l[i]]=i;}}private String n;private String o;public System_AES(String a, String b){this.n=a;this.o=b;D(a);}public byte[] A(byte[] aa, boolean bb){D(bb?n:o);byte[] cc=new byte[h];byte[] dd=new byte[h];byte[] ee;int ff, gg, hh, ii;if(aa==null){throw new IllegalArgumentException("Empty plaintext");}if(aa.length!=h){throw new IllegalArgumentException("Incorrect plaintext length : "+aa.length+" : "+new String(aa));}ee=b[0];for(ff=0;ff<h;ff++){cc[ff]=(byte)(aa[ff]^ee[ff]);}for(int r=1;r<System_AES.a;r++){ee=b[r];for(ff=0;ff<h;ff++){dd[ff]=d[cc[ff]&0xFF];}for(ff=0;ff<h;ff++){hh=ff%g;gg=(ff+(System_AES.k[hh]*g))%h;cc[ff]=dd[gg];}for(ii=0;ii<j;ii++){ff=ii*g;dd[ff]=(byte)(E(2,cc[ff])^E(3,cc[ff+1])^cc[ff+2]^cc[ff+3]);dd[ff+1]=(byte)(cc[ff]^E(2,cc[ff+1])^E(3,cc[ff+2])^cc[ff+3]);dd[ff+2]=(byte)(cc[ff]^cc[ff+1]^E(2,cc[ff+2])^E(3,cc[ff+3]));dd[ff+3]=(byte)(E(3,cc[ff])^cc[ff+1]^cc[ff+2]^E(2,cc[ff+3]));}for(ff=0;ff<h;ff++){cc[ff]=(byte)(dd[ff]^ee[ff]);}}ee=b[System_AES.a];for(ff=0;ff<h;ff++){cc[ff]=d[cc[ff]&0xFF];}for(ff=0;ff<h;ff++){hh=ff%g;gg=(ff+(System_AES.k[hh]*g))%h;dd[ff]=cc[gg];}for(ff=0;ff<h;ff++){cc[ff]=(byte)(dd[ff]^ee[ff]);}return (cc);}public byte[] B(byte[] aa, boolean bb){D(bb?n:o);byte[] cc=new byte[h];byte[] dd=new byte[h];byte[] ee;int ff, gg, hh, ii;if(aa==null){throw new IllegalArgumentException("Empty ciphertext");}if(aa.length!=h){throw new IllegalArgumentException("Incorrect ciphertext length");}ee=c[0];for(ff=0;ff<h;ff++){cc[ff]=(byte)(aa[ff]^ee[ff]);}for(int r=1;r<System_AES.a;r++){ee=c[r];for(ff=0;ff<h;ff++){hh=ff%g;gg=(ff+h-(System_AES.k[hh]*g))%h;dd[ff]=cc[gg];}for(ff=0;ff<h;ff++){cc[ff]=e[dd[ff]&0xFF];}for(ff=0;ff<h;ff++){dd[ff]=(byte)(cc[ff]^ee[ff]);}for(ii=0;ii<j;ii++){ff=ii*g;cc[ff]=(byte)(E(0x0e,dd[ff])^E(0x0b,dd[ff+1])^E(0x0d,dd[ff+2])^E(0x09,dd[ff+3]));cc[ff+1]=(byte)(E(0x09,dd[ff])^E(0x0e,dd[ff+1])^E(0x0b,dd[ff+2])^E(0x0d,dd[ff+3]));cc[ff+2]=(byte)(E(0x0d,dd[ff])^E(0x09,dd[ff+1])^E(0x0e,dd[ff+2])^E(0x0b,dd[ff+3]));cc[ff+3]=(byte)(E(0x0b,dd[ff])^E(0x0d,dd[ff+1])^E(0x09,dd[ff+2])^E(0x0e,dd[ff+3]));}}ee=c[System_AES.a];for(ff=0;ff<h;ff++){hh=ff%g;gg=(ff+h-(System_AES.k[hh]*g))%h;dd[ff]=cc[gg];}for(ff=0;ff<h;ff++){dd[ff]=e[dd[ff]&0xFF];}for(ff=0;ff<h;ff++){cc[ff]=(byte)(dd[ff]^ee[ff]);}return (cc);}private void C(byte[] aa){final int bb=h/4;final int cc=aa.length;final int dd=cc/4;int ee, ff, gg;if(!(aa.length==16||aa.length==24||aa.length==32)){throw new IllegalArgumentException("Incorrect key length");}final int hh=(this.a+1)*bb;byte[] ii=new byte[hh];byte[] jj=new byte[hh];byte[] kk=new byte[hh];byte[] ll=new byte[hh];b=new byte[this.a+1][h];c=new byte[this.a+1][h];for(ee=0,ff=0;ee<dd;ee++){ii[ee]=aa[ff++];jj[ee]=aa[ff++];kk[ee]=aa[ff++];ll[ee]=aa[ff++];}byte mm, nn, oo, pp, qq;for(ee=dd;ee<hh;ee++){mm=ii[ee-1];nn=jj[ee-1];oo=kk[ee-1];pp=ll[ee-1];if((ee%dd)==0){qq=mm;mm=(byte)(d[nn&0xFF]^f[ee/dd]);nn=d[oo&0xFF];oo=d[pp&0xFF];pp=d[qq&0xFF];}else if((dd>6)&&((ee%dd)==4)){mm=d[mm&0xFF];nn=d[nn&0xFF];oo=d[oo&0xFF];pp=d[pp&0xFF];}ii[ee]=(byte)(ii[ee-dd]^mm);jj[ee]=(byte)(jj[ee-dd]^nn);kk[ee]=(byte)(kk[ee-dd]^oo);ll[ee]=(byte)(ll[ee-dd]^pp);}for(gg=0,ee=0;gg<this.a+1;gg++){for(ff=0;ff<bb;ff++){b[gg][4*ff]=ii[ee];b[gg][4*ff+1]=jj[ee];b[gg][4*ff+2]=kk[ee];b[gg][4*ff+3]=ll[ee];c[this.a-gg][4*ff]=ii[ee];c[this.a-gg][4*ff+1]=jj[ee];c[this.a-gg][4*ff+2]=kk[ee];c[this.a-gg][4*ff+3]=ll[ee];ee++;}}}private void D(String aa){C(F(aa));}static final int E(int a, int b){return (a!=0&&b!=0)?(l[(m[a&0xFF]+m[b&0xFF])%255]):0;}public static byte[] F(String aa){byte[] bb=new byte[aa.length()];for(int i=0;i<aa.length();i++){bb[i]=(byte)aa.charAt(i);}return bb;}}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Nicnl - nicnl25@gmail.com