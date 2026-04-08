export const API_BASE_URL = 'http://localhost:8080';

export async function fetchApi(endpoint, options = {}) {
  const defaultOptions = {
    credentials: 'omit', // We change this depending on the endpoint type
    headers: {
      'Content-Type': 'application/json',
    },
  };

  if(!endpoint.startsWith('/auth')) {
     defaultOptions.credentials = 'include';
  }

  const mergedOptions = {
    ...defaultOptions,
    ...options,
    headers: {
      ...defaultOptions.headers,
      ...options.headers,
    },
  };

  const response = await fetch(`${API_BASE_URL}${endpoint}`, mergedOptions);
  
  if (!response.ok) {
    if (response.status === 401) {
      // Possibly unauthenticated, trigger some event or return a specific error
      throw new Error("Unauthorized");
    }
    const errorData = await response.text();
    throw new Error(errorData || 'An error occurred while fetching');
  }
  
  // Some endpoints return NO_CONTENT which lacks JSON body
  if (response.status === 204) {
    return null;
  }
  
  const text = await response.text();
  try {
    return text ? JSON.parse(text) : null;
  } catch (e) {
    return text; // return plain text if not JSON
  }
}
