public class Return {
	public Packet packet;
	public Type type;
	public Return(Type type) {
		this.packet=new Packet("Admin",0,0,0,Type.Wait);
		this.type=type;
	}
}
