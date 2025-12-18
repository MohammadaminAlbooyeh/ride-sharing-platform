from sqlalchemy import Column, Integer, String, Text
from .database import Base


class Contact(Base):
    __tablename__ = "contacts"
    id = Column(Integer, primary_key=True, index=True)
    name = Column(String(128), nullable=False)
    phone = Column(String(32), nullable=False)
    email = Column(String(128), nullable=True)
    notes = Column(Text, nullable=True)
