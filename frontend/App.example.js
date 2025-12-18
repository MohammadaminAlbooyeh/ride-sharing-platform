import React, { useEffect, useState } from 'react';
import { SafeAreaView, View, Text, FlatList, TextInput, Button, StyleSheet } from 'react-native';

// IMPORTANT: replace MACHINE_IP with your computer LAN IP, or set via env
const API_BASE = 'http://MACHINE_IP:8000';

export default function App() {
  const [contacts, setContacts] = useState([]);
  const [name, setName] = useState('');
  const [phone, setPhone] = useState('');

  const load = async () => {
    try {
      const res = await fetch(`${API_BASE}/contacts/`);
      const data = await res.json();
      setContacts(data);
    } catch (e) {
      console.warn('Failed to fetch contacts', e);
    }
  };

  useEffect(() => { load(); }, []);

  const add = async () => {
    if (!name || !phone) return;
    try {
      await fetch(`${API_BASE}/contacts/`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, phone }),
      });
      setName(''); setPhone('');
      load();
    } catch (e) {
      console.warn('Failed to add', e);
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <Text style={styles.title}>Contacts</Text>
      <View style={styles.form}>
        <TextInput style={styles.input} placeholder="Name" value={name} onChangeText={setName} />
        <TextInput style={styles.input} placeholder="Phone" value={phone} onChangeText={setPhone} keyboardType="phone-pad" />
        <Button title="Add" onPress={add} />
      </View>
      <FlatList
        data={contacts}
        keyExtractor={(item) => String(item.id)}
        renderItem={({ item }) => (
          <View style={styles.item}>
            <Text style={styles.itemText}>{item.name} — {item.phone}</Text>
          </View>
        )}
      />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, padding: 16 },
  title: { fontSize: 24, fontWeight: '600', marginBottom: 12 },
  form: { marginBottom: 12 },
  input: { borderWidth: 1, borderColor: '#ccc', padding: 8, marginBottom: 8, borderRadius: 4 },
  item: { padding: 8, borderBottomWidth: 1, borderBottomColor: '#eee' },
  itemText: { fontSize: 16 },
});
