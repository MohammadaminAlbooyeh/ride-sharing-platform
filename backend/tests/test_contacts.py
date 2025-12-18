import pytest
from fastapi.testclient import TestClient

def test_create_contact(client: TestClient):
    response = client.post("/contacts/", json={"name": "John Doe", "phone": "123-456-7890"})
    assert response.status_code == 200
    data = response.json()
    assert data["name"] == "John Doe"
    assert data["phone"] == "123-456-7890"
    assert "id" in data

def test_list_contacts(client: TestClient):
    # Create a contact first
    client.post("/contacts/", json={"name": "Jane Doe", "phone": "098-765-4321"})
    response = client.get("/contacts/")
    assert response.status_code == 200
    data = response.json()
    assert len(data) >= 1
    assert any(c["name"] == "Jane Doe" for c in data)

def test_get_contact(client: TestClient):
    # Create a contact
    create_response = client.post("/contacts/", json={"name": "Alice", "phone": "555-1234"})
    contact_id = create_response.json()["id"]
    response = client.get(f"/contacts/{contact_id}")
    assert response.status_code == 200
    data = response.json()
    assert data["name"] == "Alice"
    assert data["phone"] == "555-1234"

def test_get_contact_not_found(client: TestClient):
    response = client.get("/contacts/999")
    assert response.status_code == 404
    assert response.json()["detail"] == "Contact not found"

def test_update_contact(client: TestClient):
    # Create a contact
    create_response = client.post("/contacts/", json={"name": "Bob", "phone": "111-222-3333"})
    contact_id = create_response.json()["id"]
    # Update it
    response = client.put(f"/contacts/{contact_id}", json={"name": "Bob Updated", "phone": "444-555-6666"})
    assert response.status_code == 200
    data = response.json()
    assert data["name"] == "Bob Updated"
    assert data["phone"] == "444-555-6666"

def test_update_contact_not_found(client: TestClient):
    response = client.put("/contacts/999", json={"name": "Test", "phone": "000-000-0000"})
    assert response.status_code == 404
    assert response.json()["detail"] == "Contact not found"

def test_delete_contact(client: TestClient):
    # Create a contact
    create_response = client.post("/contacts/", json={"name": "Charlie", "phone": "777-888-9999"})
    contact_id = create_response.json()["id"]
    # Delete it
    response = client.delete(f"/contacts/{contact_id}")
    assert response.status_code == 204
    # Verify it's gone
    get_response = client.get(f"/contacts/{contact_id}")
    assert get_response.status_code == 404

def test_delete_contact_not_found(client: TestClient):
    response = client.delete("/contacts/999")
    assert response.status_code == 404
    assert response.json()["detail"] == "Contact not found"